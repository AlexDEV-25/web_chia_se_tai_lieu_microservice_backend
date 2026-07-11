package com.example.profileservice.service;


import com.example.AppError;
import com.example.AppException;
import com.example.ConnectionStatus;
import com.example.UserProfileUpdatedEvent;
import com.example.helper.GetUserIdByToken;
import com.example.profileservice.dto.response.UserBioProjection;
import com.example.profileservice.dto.response.UserBioResponse;
import com.example.profileservice.mapper.UserDetailMapper;
import com.example.profileservice.model.UserDetail;
import com.example.profileservice.repository.UserDetailRepository;
import com.example.profileservice.repository.httpclient.FileClient;
import com.example.request.DisplayRequest;
import com.example.request.UserDetailRequest;
import com.example.response.UserDetailInfoResponse;
import com.example.response.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserDetailRepository userDetailRepository;
    private final UserDetailMapper userMapper;
    private final FileClient fileClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PreAuthorize("hasAuthority('GET_MY_DETAIL_INFO')")
    public UserDetailResponse getDetailUser() {
        Long userId = GetUserIdByToken.get();
        UserDetail find = userDetailRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        return userMapper.userToResponse(find);
    }

    public UserBioResponse getBioUser(Long userId) {
        UserDetail find = userDetailRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        return userMapper.userToUserBioResponse(find);
    }

    public UserDetailResponse createUserDetail(UserDetailRequest dto) {
        UserDetail user = userMapper.requestToUser(dto);

        user.setCreatedAt(LocalDateTime.now());
        if (userDetailRepository.existsByUserId(dto.getUserId())) {
            throw AppException.builder().appError(AppError.EMAIL_ALREADY_EXISTS).build();
        }
        UserDetail saved = userDetailRepository.save(user);
        return userMapper.userToResponse(saved);
    }

    public UserDetailResponse hideUserDetail(Long userId, DisplayRequest dto) {
        UserDetail find = userDetailRepository.findByUserId(userId).orElseThrow(
                () -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        find.setHide(dto.isHide());
        find.setUpdatedAt(LocalDateTime.now());

        UserDetail saved = userDetailRepository.save(find);
        return userMapper.userToResponse(saved);
    }

    @PreAuthorize("hasAuthority('UPDATE_MY_DETAIL_INFO')")
    public UserDetailResponse updateMyInfo(MultipartFile avt, UserDetailRequest dto) {
        Long userId = GetUserIdByToken.get();
        UserDetail find = userDetailRepository.findByUserId(userId).orElseThrow(
                () -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        String originName = find.getFullName();
        String originAvatar = find.getAvatarUrl();
        if (find.getAvatarUrl() != null) {
            try {
                fileClient.deleteFile(find.getAvatarUrl());
            } catch (Exception e) {
                throw AppException.builder().appError(AppError.UPDATE_PROFILE_FAILED).build();
            }
        }


        find.setUpdatedAt(LocalDateTime.now());
        find.setFullName(dto.getFullName());
        find.setBio(dto.getBio());

        if (avt != null) {
            try {
                Map<String, Object> handleAvt = fileClient.uploadImage(avt).getResult();
                String avatarUrl = (String) handleAvt.get("secure_url");
                find.setAvatarUrl(avatarUrl);
            } catch (Exception e) {
                throw AppException.builder().appError(AppError.UPDATE_PROFILE_FAILED).build();
            }
        }

        UserDetail saved = userDetailRepository.save(find);

        if (!originName.equals(saved.getFullName()) || !Objects.equals(originAvatar, saved.getAvatarUrl())) {
            UserProfileUpdatedEvent userProfileUpdatedEvent = UserProfileUpdatedEvent.builder()
                    .userId(saved.getUserId())
                    .fullName(saved.getFullName())
                    .avatarUrl(saved.getAvatarUrl())
                    .build();
            kafkaTemplate.send("user-profile-updated", userProfileUpdatedEvent).whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Cannot send event", ex);
                    // sau này thích dùng @Schedule và Outbox pattern để gửi lại event
                }
            });
        }

        return userMapper.userToResponse(saved);
    }

    @PreAuthorize("hasAuthority('SEARCH_USER')")
    public List<UserBioProjection> search(String keyword) {
        return userDetailRepository.search(keyword);
    }

    public void changeConnectStatus(Long userId, ConnectionStatus status) {
        UserDetail find = userDetailRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        find.setStatus(status);
        userDetailRepository.save(find);
    }

    public UserDetailInfoResponse getUserDetailInfo(Long userId) {
        UserDetail find = userDetailRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        return userMapper.userToInfoResponse(find);
    }
}
