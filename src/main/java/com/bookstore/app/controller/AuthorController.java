package com.bookstore.app.controller;

import com.bookstore.app.dto.request.AuthorRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.AuthorResponse;
import com.bookstore.app.service.AuthorService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthorController {

    AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<AuthorResponse>>> getAll() {
        log.info("Get all authors");
        return ResponseEntity.ok(
                ApiResponse.<List<AuthorResponse>>builder()
                        .success(true)
                        .message("Get all authors")
                        .data(this.authorService.getAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> getAuthorById(@PathVariable("id") Long id) {
        log.info("Get author by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<AuthorResponse>builder()
                        .success(true)
                        .message("Get author by id successfully!")
                        .data(authorService.getAuthorById(id))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<AuthorResponse>> createAuthor(
            @Valid @RequestBody AuthorRequest authorRequest) {
        log.info("Create author: {}", authorRequest);
        return ResponseEntity.ok(
                ApiResponse.<AuthorResponse>builder()
                        .success(true)
                        .message("Author created successfully!")
                        .data(authorService.createAuthor(authorRequest))
                        .build()
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<AuthorResponse>> updateAuthor(
            @PathVariable("id") Long id,
            @Valid @RequestBody AuthorRequest authorRequest
    ) {
        log.info("Update author by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<AuthorResponse>builder()
                        .success(true)
                        .message("Author updated successfully!")
                        .data(authorService.updateAuthor(id, authorRequest))
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthorById(@PathVariable("id") Long id) {
        log.info("Delete author by id: {}", id);
        authorService.deleteAuthorById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Author deleted successfully!")
                        .data(null)
                        .build()
        );
    }
}