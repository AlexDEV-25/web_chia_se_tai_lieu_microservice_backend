package com.example.authservice.service;

import com.example.AppError;
import com.example.authservice.dto.request.ChangeEmailRequest;
import com.example.authservice.dto.request.ChangePasswordRequest;
import com.example.authservice.dto.request.RegisterRequest;
import com.example.authservice.dto.response.UserResponse;
import com.example.authservice.helper.CreateBodyEmail;
import com.example.authservice.helper.GetUserByToken;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.Role;
import com.example.authservice.model.User;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.httpclient.ProfileClient;
import com.example.commondto.request.DisplayRequest;
import com.example.commondto.request.UserDetailRequest;
import com.example.commondto.response.DailyCountProjection;
import com.example.commonexception.exception.AppException;
import com.example.event.EmailNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final GetUserByToken getUserByToken;
    private final ProfileClient profileClient;
    private final CreateBodyEmail createBodyEmail;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PreAuthorize("hasRole('ADMIN')")
    public List<DailyCountProjection> userLast7Days() {
        return userRepository.countUserByDay(LocalDate.now().minusDays(6).atStartOfDay());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::userToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createUser(RegisterRequest dto) {
        User user = userMapper.requestToUser(dto);

        List<Role> roles = roleRepository.findAllById(dto.getRoles());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw AppException.builder().appError(AppError.EMAIL_ALREADY_EXISTS).build();
        }
        if (userRepository.existsByUsername(dto.getEmail())) {
            throw AppException.builder().appError(AppError.USERNAME_ALREADY_EXISTS).build();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        try {
            profileClient.createUserDetail(UserDetailRequest.builder().userId(saved.getId()).build());
        } catch (Exception e) {
            userRepository.delete(saved);
            throw AppException.builder().appError(AppError.CREATE_PROFILE_FAILED).build();
        }

        return userMapper.userToResponse(saved);

    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse hideUser(Long id, DisplayRequest request) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        entity.setHide(request.isHide());
        entity.setUpdatedAt(LocalDateTime.now());
        User saved = userRepository.save(entity);

        try {
            profileClient.hideUserDetail(id, request);
        } catch (Exception e) {
            userRepository.delete(saved);
            throw AppException.builder().appError(AppError.CREATE_PROFILE_FAILED).build();
        }

        EmailNotificationEvent emailNotificationEvent = EmailNotificationEvent.builder()
                .channel("EMAIL")
                .recipient(saved.getEmail())
                .subject("Tài khoản của bạn tạm thời bị khóa")
                .body(createBodyEmail.bodyLockAccount())
                .build();

        kafkaTemplate.send("lock-account", emailNotificationEvent);

        return userMapper.userToResponse(saved);
    }

    @PreAuthorize("hasAuthority('GET_MY_INFO')")
    public UserResponse getMyInfo() {
        User info = getUserByToken.get();
        return userMapper.userToResponse(info);
    }

    @PreAuthorize("hasAuthority('UPDATE_EMAIL')")
    public UserResponse updateEmail(ChangeEmailRequest dto) {
        User entity = getUserByToken.get();

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setEmail(dto.getEmail());

        User saved = userRepository.save(entity);
        return userMapper.userToResponse(saved);
    }


    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    public void changePassword(ChangePasswordRequest request) {

        User user = getUserByToken.get();
        boolean isMatch = passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword()
        );
        if (isMatch) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
        } else {
            throw AppException.builder().appError(AppError.INCORRECT_PASSWORD).build();
        }
    }
}
