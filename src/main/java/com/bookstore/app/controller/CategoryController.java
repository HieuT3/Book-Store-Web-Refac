package com.bookstore.app.controller;

import com.bookstore.app.dto.request.CategoryRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.CategoryResponse;
import com.bookstore.app.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/category")
@Slf4j
public class CategoryController {
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {
        log.info("Get all categories");
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .success(true)
                        .message("Get all categories")
                        .data(this.categoryService.getAll())
                        .build()
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> findCategoryById(@PathVariable("id") Long id) {
        log.info("Get category by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Get category by id: " + id)
                        .data(this.categoryService.getCategoryById(id))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest
            ) {
        log.info("Create category: {}", categoryRequest);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category created successfully!")
                        .data(this.categoryService.createCategory(categoryRequest))
                        .build()
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody CategoryRequest categoryRequest
    ) {
        log.info("Update category by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category updated successfully!")
                        .data(categoryService.updateCategory(id, categoryRequest))
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoryById(@PathVariable("id") Long id) {
        log.info("Delete category by id: {}", id);
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category deleted successfully!")
                        .data(null)
                        .build()
        );
    }
}
