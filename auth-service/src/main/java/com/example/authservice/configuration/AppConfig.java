package com.example.authservice.configuration;


import com.example.authservice.model.Permission;
import com.example.authservice.model.Role;
import com.example.authservice.repository.PermissionRepository;
import com.example.authservice.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    RestTemplate restTemplate() {
        // dùng để gọi api từ spring
        // đang dùng trong fileManager
        return new RestTemplate();
    }

    @Bean
    ApplicationRunner applicationRunner(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        return args -> {
            permissionsSave(permissionRepository);
            rolesSave(roleRepository, permissionRepository);
        };
    }

    private void permissionsSave(PermissionRepository permissionRepository) {
        checkPermissionAndSave(permissionRepository, "GET_MESSAGE", "lấy tin nhắn");
        checkPermissionAndSave(permissionRepository, "CREATE_MESSAGE", "tạo tin nhắn");
        checkPermissionAndSave(permissionRepository, "CHAT_GEMINI", "chat với AI");
        checkPermissionAndSave(permissionRepository, "HISTORY_CHAT_GEMINI", "lấy lịch sử chat với AI");
        checkPermissionAndSave(permissionRepository, "POST_COMMENT", "đăng bình luận");
        checkPermissionAndSave(permissionRepository, "UPDATE_MY_COMMENT", "sửa bình luận");
        checkPermissionAndSave(permissionRepository, "GET_MY_CONVERSATION", "lấy các cuộc hội thoại mình tham dự");
        checkPermissionAndSave(permissionRepository, "CREATE_DIRECT_CONVERSATION", "tạo cuộc hộ thoại 1 vs 1");
        checkPermissionAndSave(permissionRepository, "CREATE_GROUP_CONVERSATION", "tạo nhóm chat");
        checkPermissionAndSave(permissionRepository, "SEARCH_CONVERSATION", "tìm kiếm cuộc hội thoại");
        checkPermissionAndSave(permissionRepository, "GET_DETAIL_CONVERSATION",
                "lấy thông tin chi tiết của cuộc hội thoại");
        checkPermissionAndSave(permissionRepository, "GET_MY_DOCUMENT", "lấy tài liệu mình đăng");
        checkPermissionAndSave(permissionRepository, "GET_MY_DOCUMENT_DETAIL", "lấy chi tiết tài liệu mình đăng");
        checkPermissionAndSave(permissionRepository, "UPDATE_MY_DOCUMENT", "sửa tài liệu của mình");
        checkPermissionAndSave(permissionRepository, "DELETE_MY_DOCUMENT", "xóa tài liệu của mình");
        checkPermissionAndSave(permissionRepository, "COUNT_MY_DOCUMENT", "đếm số tài liệu của mình");
        checkPermissionAndSave(permissionRepository, "UPLOAD_FILE", "đăng tài liệu");
        checkPermissionAndSave(permissionRepository, "DOWNLOAD_FILE", "tải tài liệu");
        checkPermissionAndSave(permissionRepository, "ADD_FAVORITE", "thêm vào kho yêu thích");
        checkPermissionAndSave(permissionRepository, "GET_FAVORITE", "lấy danh sách nhưng tài liệu trong kho");
        checkPermissionAndSave(permissionRepository, "REMOVE_FAVORITE", "xóa tài liệu khỏi kho");
        checkPermissionAndSave(permissionRepository, "CHECK_FAVORITE", "kiểm tra xem lưu tài liệu vào kho chưa");
        checkPermissionAndSave(permissionRepository, "UPDATE_LAST_SEEN", "cập nhật lần cuối đăng nhập");
        checkPermissionAndSave(permissionRepository, "ADD_MEMBER", "thêm thành viên vào nhóm chat");
        checkPermissionAndSave(permissionRepository, "DELETE_MEMBER", "xóa thành viên khỏi nhóm chat");
        checkPermissionAndSave(permissionRepository, "CHANGE_ROLE", "thay đổi vai trò thành viên nhóm chat");
        checkPermissionAndSave(permissionRepository, "GET_MY_RATING", "lấy đánh giá tài liệu của mình");
        checkPermissionAndSave(permissionRepository, "POST_RATING", "đánh giá");
        checkPermissionAndSave(permissionRepository, "REPORT", "báo cáo");
        checkPermissionAndSave(permissionRepository, "FOLLOW", "kết bạn");
        checkPermissionAndSave(permissionRepository, "UNFOLLOW", "hủy kết bạn");
        checkPermissionAndSave(permissionRepository, "GET_LIST_FOLLOWING",
                "lấy danh sách những những người mình follow");
        checkPermissionAndSave(permissionRepository, "GET_LIST_FOLLOWER", "lấy danh sách những người follow mình");
        checkPermissionAndSave(permissionRepository, "GET_MY_FOLLOW_COUNT", "đếm số người follow mình");
        checkPermissionAndSave(permissionRepository, "CHECK_FOLLOWED", "kiểm tra xem mình follow chưa");
        checkPermissionAndSave(permissionRepository, "CHECK_IS_ME", "kiểm tra xem có phải mình không");
        checkPermissionAndSave(permissionRepository, "GET_ALL_USER_NOTIFICATION", "lấy tất cả thông báo của mình");
        checkPermissionAndSave(permissionRepository, "GET_UNREAD_USER_NOTIFICATION", "lấy những thoog báo chưa đọc");
        checkPermissionAndSave(permissionRepository, "READ_NOTIFICATION", "xác nhận đọc thông báo");
        checkPermissionAndSave(permissionRepository, "READ_ALL_NOTIFICATION", "xác nhận đọc tất cả thông báo");
        checkPermissionAndSave(permissionRepository, "GET_MY_INFO", "lấy thông tin của mình");
        checkPermissionAndSave(permissionRepository, "GET_MY_DETAIL_INFO", "lấy thông tin chi tiết của mình");
        checkPermissionAndSave(permissionRepository, "UPDATE_MY_DETAIL_INFO", "cập nhật thông tin chi tiết của mình");
        checkPermissionAndSave(permissionRepository, "UPDATE_EMAIL", "thay đổi email của mình");
        checkPermissionAndSave(permissionRepository, "SEARCH_USER", "tìm kiếm người dùng");
        checkPermissionAndSave(permissionRepository, "CHANGE_PASSWORD", "thay đổi password");
    }

    private void rolesSave(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        checkRoleAndSave(roleRepository, "ADMIN", "quản trị viên", null);
        checkRoleAndSave(roleRepository, "USER", "người dùng ", permissionRepository.findAll());
    }

    private void checkPermissionAndSave(PermissionRepository permissionRepository, String permission,
                                        String description) {

        if (permissionRepository.findByName(permission).isEmpty()) {
            permissionRepository.save(Permission.builder().name(permission).description(description).build());
        }
    }

    private void checkRoleAndSave(RoleRepository roleRepository, String role, String description,
                                  List<Permission> permissions) {

        if (roleRepository.findByName(role).isEmpty()) {
            roleRepository.save(Role.builder().name(role).description(description).permissions(permissions).build());
        }
    }
}
