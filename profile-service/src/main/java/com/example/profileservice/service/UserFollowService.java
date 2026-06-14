package com.example.profileservice.service;


import com.example.profileservice.constant.AppError;
import com.example.profileservice.dto.response.FollowCountResponse;
import com.example.profileservice.dto.response.UserFollowResponse;
import com.example.profileservice.exception.AppException;
import com.example.profileservice.helper.GetUserIdByToken;
import com.example.profileservice.mapper.UserFollowMapper;
import com.example.profileservice.model.UserFollow;
import com.example.profileservice.repository.UserDetailRepository;
import com.example.profileservice.repository.UserFollowRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserFollowService {
    private final UserFollowRepository userFollowRepository;
    private final UserDetailRepository userDetailRepository;
    //    private final ApplicationEventPublisher eventPublisher;
    private final UserFollowMapper userFollowMapper;
    private final GetUserIdByToken getUserIdByToken;

    @PreAuthorize("hasAuthority('FOLLOW')")
    @Transactional
    public UserFollowResponse save(Long followingId) {
        Long followerId = getUserIdByToken.get();

        if (!userDetailRepository.existsByUserId(followingId)) {
            throw AppException.builder().appError(AppError.USER_NOT_FOUND).build();
        }
        if (userFollowRepository.existsByFollowerIdAndFollowingId(followerId, followingId)) {
            throw AppException.builder().appError(AppError.ALREADY_FRIEND).build();
        }
        if (followerId.equals(followingId)) {
            throw AppException.builder().appError(AppError.CANNOT_FOLLOW_YOURSELF).build();
        }

        UserFollow userFollow = UserFollow.builder().followerId(followerId).followingId(followingId)
                .createdAt(LocalDateTime.now()).build();

        UserFollow saved = userFollowRepository.save(userFollow);

        UserFollowResponse response = userFollowMapper.userFollowToResponse(saved);

//        if (response != null) {
//            eventPublisher.publishEvent(new UserFollowCreateEvent(follower, following));
//        }
        return response;
    }

    @PreAuthorize("hasAuthority('UNFOLLOW')")
    @Transactional
    public void delete(Long followingId) {
        Long followerId = getUserIdByToken.get();
        userFollowRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);

    }

    // lấy danh sách người mình theo dõi
    @PreAuthorize("hasAuthority('GET_LIST_FOLLOWING')")
    public List<UserFollowResponse> getFollowingByFollower() {
        Long followerId = getUserIdByToken.get();
        List<UserFollow> userFollows = userFollowRepository.findByFollowerId(followerId);
        return userFollows.stream().map(userFollowMapper::userFollowToResponse).toList();
    }

    // lấy danh sách người theo dõi mình
    @PreAuthorize("hasAuthority('GET_LIST_FOLLOWER')")
    public List<UserFollowResponse> getFollowerByFollowing() {
        Long followerId = getUserIdByToken.get();
        List<UserFollow> userFollows = userFollowRepository.findByFollowingId(followerId);
        return userFollows.stream().map(userFollowMapper::userFollowToResponse).toList();
    }

    @PreAuthorize("hasAuthority('GET_MY_FOLLOW_COUNT')")
    public FollowCountResponse getMyFollowCount() {
        Long userId = getUserIdByToken.get();
        Long follower = userFollowRepository.countByFollowingId(userId);
        Long following = userFollowRepository.countByFollowerId(userId);
        return FollowCountResponse.builder().follower(follower).following(following).build();
    }

    @PreAuthorize("hasAuthority('CHECK_FOLLOWED')")
    public boolean checkFollowed(Long followingId) {
        Long userId = getUserIdByToken.get();
        return userFollowRepository.existsByFollowerIdAndFollowingId(userId, followingId);
    }

    @PreAuthorize("hasAuthority('CHECK_IS_ME')")
    public boolean checkIsMe(Long followingId) {
        Long userId = getUserIdByToken.get();
        return userId.equals(followingId);
    }

    public FollowCountResponse getFollowCount(Long userId) {
        Long follower = userFollowRepository.countByFollowingId(userId);
        Long following = userFollowRepository.countByFollowerId(userId);
        return FollowCountResponse.builder().follower(follower).following(following).build();
    }
}
