package com.example.profileservice.controller;

import com.example.profileservice.dto.request.UserDetailRequest;
import com.example.profileservice.dto.response.APIResponse;
import com.example.profileservice.dto.response.UserDetailResponse;
import com.example.profileservice.service.UserDetailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/external/users-detail")
@AllArgsConstructor
public class ExternalUserDetailController {
    private final UserDetailService userService;

    @PostMapping(value = "/my-info")
    public APIResponse<UserDetailResponse> createUserDetail(@RequestBody @Valid UserDetailRequest dto) {
        APIResponse<UserDetailResponse> apiResponse = new APIResponse<UserDetailResponse>();
        apiResponse.setResult(userService.save(dto));
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
