package com.example.profileservice.controller;


import com.example.commondto.response.APIResponse;
import com.example.profileservice.dto.response.FollowCountResponse;
import com.example.profileservice.dto.response.UserFollowResponse;
import com.example.profileservice.service.UserFollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/external/follows")
@AllArgsConstructor
public class ExternalUserFollowController {
    private final UserFollowService userFollowService;

    @PostMapping("/{followingId}")
    public APIResponse<UserFollowResponse> save(@PathVariable Long followingId) {
        APIResponse<UserFollowResponse> apiResponse = new APIResponse<UserFollowResponse>();
        apiResponse.setResult(userFollowService.save(followingId));
        return apiResponse;
    }

    @DeleteMapping("/{followingId}")
    public APIResponse<Void> delete(@PathVariable Long followingId) {
        userFollowService.delete(followingId);
        return APIResponse.<Void>builder().build();
    }

    @GetMapping("/following")
    public APIResponse<UserFollowResponse> getFollowing() {
        APIResponse<UserFollowResponse> apiResponse = new APIResponse<UserFollowResponse>();
        apiResponse.setResultList(userFollowService.getFollowingByFollower());
        return apiResponse;
    }

    @GetMapping("/follower")
    public APIResponse<UserFollowResponse> getFollower() {
        APIResponse<UserFollowResponse> apiResponse = new APIResponse<UserFollowResponse>();
        apiResponse.setResultList(userFollowService.getFollowerByFollowing());
        return apiResponse;
    }

    @GetMapping("/my-follow-count")
    public APIResponse<FollowCountResponse> getMyFollowCount() {
        APIResponse<FollowCountResponse> apiResponse = new APIResponse<FollowCountResponse>();
        apiResponse.setResult(userFollowService.getMyFollowCount());
        return apiResponse;
    }

    @GetMapping("/follow-count/{userId}")
    public APIResponse<FollowCountResponse> getFollowCount(@PathVariable Long userId) {
        APIResponse<FollowCountResponse> apiResponse = new APIResponse<FollowCountResponse>();
        apiResponse.setResult(userFollowService.getFollowCount(userId));
        return apiResponse;
    }

    @GetMapping("/check/{userId}")
    public APIResponse<Boolean> checkFollowed(@PathVariable Long userId) {
        APIResponse<Boolean> apiResponse = new APIResponse<Boolean>();
        apiResponse.setResult(userFollowService.checkFollowed(userId));
        return apiResponse;
    }

    @GetMapping("/check-is-me/{userId}")
    public APIResponse<Boolean> checkIsMe(@PathVariable Long userId) {
        APIResponse<Boolean> apiResponse = new APIResponse<Boolean>();
        apiResponse.setResult(userFollowService.checkIsMe(userId));
        return apiResponse;
    }

}
