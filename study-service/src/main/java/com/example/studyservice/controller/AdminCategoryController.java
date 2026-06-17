package com.example.studyservice.controller;


import com.example.studyservice.dto.request.CategoryRequest;
import com.example.studyservice.dto.request.DisplayRequest;
import com.example.studyservice.dto.response.APIResponse;
import com.example.studyservice.dto.response.CategoryResponse;
import com.example.studyservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories/admin")
@AllArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public APIResponse<CategoryResponse> getById(@PathVariable Long id) {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResult(categoryService.findById(id));
        return apiResponse;
    }

    @GetMapping
    public APIResponse<CategoryResponse> getAllCategory() {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResultList(categoryService.getAllCategories());
        return apiResponse;
    }

    @PostMapping
    public APIResponse<CategoryResponse> create(@RequestBody @Valid CategoryRequest dto) {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResult(categoryService.save(dto));
        return apiResponse;
    }

    @PutMapping("/{id}")
    public APIResponse<CategoryResponse> update(@PathVariable Long id, @RequestBody @Valid CategoryRequest dto) {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResult(categoryService.update(id, dto));
        return apiResponse;
    }

    @PutMapping("/hide/{id}")
    public APIResponse<CategoryResponse> hide(@PathVariable Long id, @RequestBody @Valid DisplayRequest dto) {
        APIResponse<CategoryResponse> apiResponse = new APIResponse<CategoryResponse>();
        apiResponse.setResult(categoryService.display(id, dto));
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return APIResponse.<Void>builder().build();
    }
}
