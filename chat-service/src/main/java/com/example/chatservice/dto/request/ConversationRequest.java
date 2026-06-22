package com.example.chatservice.dto.request;

import com.example.ConversationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConversationRequest {
    @NotNull(message = "type không được null")
    private ConversationType type;

    @Size(min = 1)
    @NotNull(message = "list không được null")
    private List<Long> otherUserIds;

}
