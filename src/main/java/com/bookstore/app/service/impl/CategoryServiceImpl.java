package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.CategoryRequest;
import com.bookstore.app.dto.response.CategoryResponse;
import com.bookstore.app.entity.Category;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.repository.CategoryRepository;
import com.bookstore.app.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    BookRepository bookRepository;
    ModelMapper modelMapper;

    @Cacheable(value = "categories", key = "'all'")
    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> {
                    Long totalBooks = bookRepository.countBooksByCategories(Set.of(category));
                    CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
                    categoryResponse.setTotalBooks(totalBooks);
                    return categoryResponse;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Long totalBooks = bookRepository.countBooksByCategories(Set.of(category));
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        categoryResponse.setTotalBooks(totalBooks);
        return categoryResponse;

    }

    @Cacheable(value = "categories", key = "'featured'")
    @Override
    public List<CategoryResponse> getCategoriesFeatured() {
        return categoryRepository.findCategoriesFeatured()
                .stream()
                .map(category -> modelMapper.map(category, CategoryResponse.class))
                .collect(Collectors.toCollection(ArrayList::new));
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
