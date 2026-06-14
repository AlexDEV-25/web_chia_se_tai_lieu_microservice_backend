package com.example.profileservice.controller;

import com.example.profileservice.dto.request.UserDetailRequest;
import com.example.profileservice.dto.response.APIResponse;
import com.example.profileservice.dto.response.DisplayRequest;
import com.example.profileservice.dto.response.UserDetailResponse;
import com.example.profileservice.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/users-detail")
@AllArgsConstructor
public class InternalUserDetailController {
    private final UserDetailService userService;

    @PostMapping(value = "/my-detail-info")
    public APIResponse<UserDetailResponse> createUserDetail(@RequestBody @Valid UserDetailRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
        apiResponse.setResult(userService.createUserDetail(dto));
        return apiResponse;
    }

    @PutMapping(value = "/my-detail-info/{userId}")
    public APIResponse<UserDetailResponse> hideUserDetail(@PathVariable Long userId, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
        apiResponse.setResult(userService.hideUserDetail(userId, dto));
        return apiResponse;
    }

    //    @PutMapping(value = "/my-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public APIResponse<UserResponse> updateMyInfo(@RequestPart(value = "avt", required = false) MultipartFile avt,
//                                                  @RequestPart("data") String dataJson) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            ChangeUserInfoRequest dto = mapper.readValue(dataJson, ChangeUserInfoRequest.class);
//
//            APIResponse<UserResponse> apiResponse = new APIResponse<UserResponse>();
//            apiResponse.setResult(userService.updateMyinfo(avt, dto));
//            return apiResponse;
//
//        } catch (Exception e) {
//            throw AppException.builder().appError(AppError.INVALID_JSON_FORMAT).build();
//        }
//    }


}
