package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.CategoryRequest;
import com.bookstore.app.dto.response.CategoryResponse;
import com.bookstore.app.entity.Category;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.CategoryRepository;
import com.bookstore.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    ModelMapper modelMapper;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        categoryRepository.findByName(categoryRequest.getName())
                .ifPresent(category ->{
                    throw new ResourceAlreadyExistsException("Category already exists");
                });
        Category savedCategory = categoryRepository.save(modelMapper.map(categoryRequest, Category.class));
        return modelMapper.map(savedCategory, CategoryResponse.class);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        existingCategory.setName(categoryRequest.getName());
        return modelMapper.map(categoryRepository.save(existingCategory), CategoryResponse.class);
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(category -> categoryRepository.deleteById(id),
                        () -> {
                    throw new ResourceNotFoundException("Category not found");
                        });
    }
}
