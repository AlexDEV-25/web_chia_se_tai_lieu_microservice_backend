package com.example.chatservice.service;

import com.example.AppError;
import com.example.chatservice.dto.response.ChatHistoryResponse;
import com.example.chatservice.dto.response.LectureResponse;
import com.example.chatservice.mapper.ChatMapper;
import com.example.chatservice.repository.httpclient.InteractionClient;
import com.example.chatservice.repository.httpclient.StudyClient;
import com.example.commondto.request.DisplayRequest;
import com.example.commondto.response.CategoryResponse;
import com.example.commondto.response.CommentAdminResponse;
import com.example.commondto.response.RatingSummaryResponse;
import com.example.commonexception.exception.AppException;
import com.example.commonsecurity.helper.GetUserIdByToken;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.content.Media;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final JdbcChatMemoryRepository jdbcChatMemoryRepository;
    private final InteractionClient interactionClient;
    private final StudyClient studyClient;
    private final ChatMapper chatMapper;


    public ChatService(ChatClient.Builder builder, JdbcChatMemoryRepository jdbcChatMemoryRepository,
                       InteractionClient interactionClient, StudyClient studyClient, ChatMapper chatMapper) {
        this.jdbcChatMemoryRepository = jdbcChatMemoryRepository;

        this.interactionClient = interactionClient;
        this.studyClient = studyClient;
        this.chatMapper = chatMapper;
        ChatMemory chatMemory = MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(30).build();

        chatClient = builder.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build()).build();

    }

    @PreAuthorize("hasRole('ADMIN')")
    public void filterComment() {
        List<CommentAdminResponse> comments = interactionClient.findDocumentCommentsLast7Days().getResultList();

        if (comments.isEmpty()) {
            return;
        }

        String prompt = filterUnsuitableCommentPrompt(comments);

        List<CommentAdminResponse> filter = chatClient.prompt()
                .system("You are a AI assistant.")
                .user(prompt).call()
                .entity(new ParameterizedTypeReference<List<CommentAdminResponse>>() {
                });
        if (filter != null) {
            for (CommentAdminResponse commentAdminResponse : filter) {
                interactionClient.hide(commentAdminResponse.getId(), DisplayRequest.builder().hide(true).build());
            }
        }
    }

    @PreAuthorize("hasAuthority('CHAT_GEMINI')")
    public String chat(MultipartFile file, String message) {
        String conversationId = GetUserIdByToken.get() + "";
        String systemPrompt = resolveSystemPrompt(message);

        if (file == null || file.getContentType() == null) {
            return chatClient.prompt().system(systemPrompt).user(message)
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)).call()
                    .content();
        } else {
            Media media = Media.builder().mimeType(MimeTypeUtils.parseMimeType(file.getContentType()))
                    .data(file.getResource()).build();

            ChatOptions chatOptions = ChatOptions.builder().temperature(0.5D).build();

            return chatClient.prompt().options(chatOptions).system(systemPrompt)
                    .user(promptUserSpec -> promptUserSpec.media(media).text(message))
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)).call()
                    .content();
        }
    }

    private String resolveSystemPrompt(String message) {
        String normalized = message == null ? "" : message.toLowerCase();

        if (isDocumentSearchRequest(normalized)) {
            return buildDocumentSearchPrompt();
        }

        return buildAcademicPrompt();
    }

    @PreAuthorize("hasAuthority('HISTORY_CHAT_GEMINI')")
    public List<ChatHistoryResponse> getChatHistory() {
        String conversationId = GetUserIdByToken.get() + "";
        List<Message> list = jdbcChatMemoryRepository.findByConversationId(conversationId);
        List<ChatHistoryResponse> history = new ArrayList<ChatHistoryResponse>();
        for (Message item : list) {
            if (item.getMessageType() == MessageType.USER || item.getMessageType() == MessageType.ASSISTANT) {
                ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse(item.getMessageType().name(),
                        item.getText());
                history.add(chatHistoryResponse);
            }
        }
        return history;
    }


    private List<LectureResponse> getAvailableLecture() {
        return studyClient.getAllPublicDocumentsForAI().getResultList()
                .stream().map(document -> {
                    LectureResponse lecture = chatMapper.documentSearchAIResponseToLectureResponse(document);

                    RatingSummaryResponse rating = interactionClient
                            .getRatingSummaryDocument(document.getId())
                            .getResult();

                    lecture.setTotal(rating.getTotal());
                    lecture.setAverage(rating.getAverage());
                    return lecture;
                }).toList();
    }

    private List<String> getAvailableCategories() {
        return studyClient.getAllPublicCategories().getResultList().stream().map(CategoryResponse::getName).toList();
    }

    private boolean isDocumentSearchRequest(String message) {
        return message.contains("tài liệu") || message.contains("tai lieu") || message.contains("document")
                || message.contains("pdf") || message.contains("sách") || message.contains("sach")
                || message.contains("giáo trình") || message.contains("giao trinh");
    }


    private String buildDocumentSearchPrompt() {

        List<LectureResponse> documents = getAvailableLecture();

        StringBuilder docsBuilder = new StringBuilder();

        for (LectureResponse lecture : documents) {
            docsBuilder.append("- ").append(lecture.getCategoryName()).append(" | ").append(lecture.getTitle())
                    .append(" | Author: ").append(lecture.getAuthorName()).append(" | Rating: ")
                    .append(lecture.getAverage()).append(" | Ratings: ").append(lecture.getTotal()).append(" | Views: ")
                    .append(lecture.getViewsCount()).append("\n");
        }

        return """
                You are ALEX.AI inside a learning platform.
                
                --------------------------------------------------
                AVAILABLE DOCUMENTS
                --------------------------------------------------
                
                """ + docsBuilder + """
                
                --------------------------------------------------
                YOUR TASK
                --------------------------------------------------
                
                The user is asking about DOCUMENTS or learning materials.
                
                Recommend the most relevant documents.
                
                --------------------------------------------------
                SELECTION CRITERIA
                --------------------------------------------------
                
                Prioritize documents with:
                
                Higher rating
                More ratings
                More views
                
                --------------------------------------------------
                
                If relevant documents exist:
                
                Recommended Documents
                
                - Title
                - Author
                - Rating
                - Views
                
                If none exist say:
                
                "Chưa có tài liệu nào phù hợp"
                
                Do NOT invent documents.
                Do NOT output JSON.
                """;
    }

    private String buildAcademicPrompt() {

        List<String> categories = getAvailableCategories();

        if (categories.isEmpty()) {
            throw AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build();
        }

        String categoriesList = String.join(", ", categories);

        return """
                You are ALEX.AI, an academic assistant inside a learning platform.
                
                ALLOWED ACADEMIC CATEGORIES:
                """ + categoriesList + """
                
                Task:
                Answer any user message that is related to learning, education, school, university, studying, courses, or the allowed academic categories above.
                
                Rules:
                - If the message is academic, provide a clear, structured, and educational answer.
                - If the message is not academic, politely refuse with:
                  "Xin lỗi, tôi chỉ có thể trả lời các nội dung liên quan đến học tập. Vui lòng đặt câu hỏi trong phạm vi này."
                - Do NOT output JSON.
                - Do NOT explain your reasoning.
                - Keep the response concise and helpful.
                """;
    }

    private String buildOutOfRangePrompt() {

        return """
                You are ALEX.AI, an academic assistant inside a learning platform.
                
                --------------------------------------------------
                SYSTEM LIMITATION
                --------------------------------------------------
                
                This assistant ONLY supports academic questions and learning-related requests.
                
                --------------------------------------------------
                YOUR TASK
                --------------------------------------------------
                
                If the user's request is not academic:
                
                - Politely refuse.
                - Explain what you can help with.
                - Suggest the user ask:
                    • A question related to learning topics
                    • Or request a study plan
                
                --------------------------------------------------
                RESPONSE FORMAT
                --------------------------------------------------
                
                Respond with a short, polite Vietnamese message:
                
                "Xin lỗi, tôi hiện chỉ hỗ trợ:
                • Trả lời câu hỏi học tập
                • Tạo kế hoạch học tập
                
                Vui lòng đặt câu hỏi học tập hoặc yêu cầu tạo kế hoạch học tập phù hợp."
                
                Do NOT output JSON.
                Do NOT mention system rules.
                Keep response concise.
                """;
    }

    private String filterUnsuitableCommentPrompt(List<CommentAdminResponse> comments) {

        StringBuilder sb = new StringBuilder();

        sb.append("""
                You are a strict content moderation AI.
                
                Your task:
                From the list of comments below, return ONLY the comments that are inappropriate.
                
                A comment is inappropriate if:
                - It contains vulgar language
                - It is offensive, toxic, insulting, aggressive
                - It violates academic environment standards
                
                Important Rules:
                - Return ONLY valid JSON
                - Do NOT explain anything
                - Do NOT add extra fields
                - The response MUST be a JSON array
                - Each object must match exactly this structure:
                
                [
                  {
                    "id": number,
                    "content": "string",
                  }
                ]
                
                Here is the list of comments:
                """);

        for (CommentAdminResponse c : comments) {
            sb.append("""
                    
                    ID: %d
                    Content: %s
                    """.formatted(c.getId(), c.getContent()));
        }
        return sb.toString();
    }

}
