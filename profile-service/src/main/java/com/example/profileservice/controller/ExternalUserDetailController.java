package com.example.profileservice.controller;

import com.example.AppError;
import com.example.commondto.request.UserDetailRequest;
import com.example.commondto.response.APIResponse;
import com.example.commondto.response.UserDetailResponse;
import com.example.commonexception.exception.AppException;
import com.example.profileservice.dto.response.UserBioProjection;
import com.example.profileservice.dto.response.UserBioResponse;
import com.example.profileservice.service.UserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/external/users-detail")
@AllArgsConstructor
public class ExternalUserDetailController {
    private final UserDetailService userService;

    @GetMapping("/bio-info/{userId}")
    public APIResponse<UserBioResponse> getInfo(@PathVariable Long userId) {
        APIResponse<UserBioResponse> apiResponse = new APIResponse<UserBioResponse>();
        apiResponse.setResult(userService.getBioUser(userId));
        return apiResponse;
    }

    @GetMapping("/my-detail-info")
    public APIResponse<UserDetailResponse> getDetailUser() {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
        apiResponse.setResult(userService.getDetailUser());
        return apiResponse;
    }

    @GetMapping("/search")
    public APIResponse<UserBioProjection> search(@RequestParam String keyword) {
        APIResponse<UserBioProjection> apiResponse = new APIResponse<>();
        apiResponse.setResultList(userService.search(keyword));
        return apiResponse;
    }

    @PutMapping(value = "/my-detail-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public APIResponse<UserDetailResponse> create(@RequestPart("file") MultipartFile file,
                                                  @RequestPart("data") String dataJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            UserDetailRequest dto = mapper.readValue(dataJson, UserDetailRequest.class);

            APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
            apiResponse.setResult(userService.updateMyInfo(file, dto));
            return apiResponse;

        } catch (Exception e) {
            throw AppException.builder().appError(AppError.INVALID_JSON_FORMAT).build();
        }
    }

}
