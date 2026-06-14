package com.example.profileservice.service;


import com.example.profileservice.constant.AppError;
import com.example.profileservice.constant.ConnectionStatus;
import com.example.profileservice.dto.request.UserDetailRequest;
import com.example.profileservice.dto.response.DisplayRequest;
import com.example.profileservice.dto.response.UserBioResponse;
import com.example.profileservice.dto.response.UserDetailResponse;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.mapper.UserDetailMapper;
import com.example.profileservice.model.UserDetail;
import com.example.profileservice.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService {
    private final UserDetailRepository userRepository;

    private final UserDetailMapper userMapper;
//    private final FileManager fileStorage;

    @PreAuthorize("hasAuthority('GET_MY_DETAIL_INFO')")
    public UserDetailResponse getDetailUser(Long userId) {
        UserDetail find = userRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        return userMapper.userToResponse(find);
    }

    public UserBioResponse getBioUser(Long userId) {
        UserDetail find = userRepository.findByUserIdAndHideFalse(userId)
                .orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        return userMapper.userToUserBioResponse(find);
    }

    public UserDetailResponse createUserDetail(UserDetailRequest dto) {
        UserDetail user = userMapper.requestToUser(dto);

        user.setCreatedAt(LocalDateTime.now());
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw AppException.builder().appError(AppError.EMAIL_ALREADY_EXISTS).build();
        }
        UserDetail saved = userRepository.save(user);
        return userMapper.userToResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailResponse hideUserDetail(Long userId, DisplayRequest dto) {
        UserDetail find = userRepository.findByUserId(userId).orElseThrow(
                () -> AppException.builder().appError(AppError.USER_NOT_FOUND).build()
        );
        find.setHide(dto.isHide());
        find.setUpdatedAt(LocalDateTime.now());

        UserDetail saved = userRepository.save(find);
        return userMapper.userToResponse(saved);
    }

//    @PreAuthorize("hasAuthority('UPDATE_MY_INFO')")
//    public UserResponse updateMyinfo(MultipartFile avt, ChangeUserInfoRequest dto) {
//        User entity = getUserByToken.get();
//        entity.setUpdatedAt(LocalDateTime.now());
//        if (avt != null) {
//            try {
//                Map<?, ?> handleAvt = fileStorage.uploadImage(avt);
//                String avatarUrl = (String) handleAvt.get("secure_url");
//
//                entity.setAvatarUrl(avatarUrl);
//            } catch (Exception e) {
//                throw AppException.builder().appError(AppError.UPDATE_PROFILE_FAILED).build();
//            }
//
//        }
//        userMapper.updateUserInfo(entity, dto);
//
//        User saved = userRepository.save(entity);
//        return userMapper.userToResponse(saved);
//    }

    @PreAuthorize("hasAuthority('SEARCH_USER')")
    public List<UserBioResponse> search(String keyword) {
        return userRepository.search(keyword);
    }

    public void changeConnectStatus(Long userId, ConnectionStatus status) {
        UserDetail find = userRepository.findByUserIdAndHideFalse(userId).orElseThrow(() -> AppException.builder().appError(AppError.USER_NOT_FOUND).build());
        find.setStatus(status);
        userRepository.save(find);
    }

}
