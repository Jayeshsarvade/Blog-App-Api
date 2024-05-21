package com.blog.app.project.blogappapi.service;

import com.blog.app.project.blogappapi.dto.CategoryDto;
import com.blog.app.project.blogappapi.payload.CategoryResponse;

public interface CategoryService {

    public CategoryDto createCategory(CategoryDto categoryDto);
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId);
    public void deleteCategory(Integer categoryId);
    public CategoryDto getCategory(Integer categoryId);
    public CategoryResponse getAllCategory(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
}
