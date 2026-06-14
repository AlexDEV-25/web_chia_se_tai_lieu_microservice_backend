package com.example.studyservice.service;


import com.example.studyservice.constant.AppError;
import com.example.studyservice.dto.request.CategoryRequest;
import com.example.studyservice.dto.request.DisplayRequest;
import com.example.studyservice.dto.respone.CategoryResponse;
import com.example.studyservice.exception.AppException;
import com.example.studyservice.mapper.CategoryMapper;
import com.example.studyservice.model.Category;
import com.example.studyservice.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(categoryMapper::entityToResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse findById(Long id) {
        Category find = categoryRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build());
        return categoryMapper.entityToResponse(find);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse save(CategoryRequest request) {
        Category category = Category.builder().name(request.getName()).description(request.getDescription()).hide(false)
                .build();
        Category saved = categoryRepository.save(category);
        return categoryMapper.entityToResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build());
        categoryMapper.updateCategory(category, request);
        Category saved = categoryRepository.save(category);
        return categoryMapper.entityToResponse(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse display(Long id, DisplayRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> AppException.builder().appError(AppError.CATEGORY_NOT_FOUND).build());
        category.setHide(request.isHide());
        Category saved = categoryRepository.save(category);
        return categoryMapper.entityToResponse(saved);


    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryResponse> getAllPublicCategories() {
        List<Category> categories = categoryRepository.findByHideFalse();
        return categories.stream().map(categoryMapper::entityToResponse).toList();
    }

}
