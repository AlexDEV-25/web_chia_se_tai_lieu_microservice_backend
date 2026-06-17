package com.example.studyservice.controller;


import com.example.studyservice.dto.response.APIResponse;
import com.example.studyservice.dto.response.CategoryResponse;
import com.example.studyservice.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public APIResponse<CategoryResponse> getAllPublicCategories() {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResultList(categoryService.getAllPublicCategories());
        return apiResponse;
    }

}
