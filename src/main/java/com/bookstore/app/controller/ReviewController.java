package com.bookstore.app.controller;

import com.bookstore.app.dto.request.ReviewRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.ReviewResponse;
import com.bookstore.app.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ReviewController {
    ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAll() {
        log.info("Fetching all reviews");
        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponse>>builder()
                        .success(true)
                        .message("Reviews fetched successfully")
                        .data(reviewService.getAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getReviewById(Long id) {
        log.info("Fetching review with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .success(true)
                        .message("Review fetched successfully")
                        .data(reviewService.getReviewById(id))
                        .build()
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByBookId(@PathVariable Long bookId) {
        log.info("Fetching reviews for book with id: {}", bookId);
        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponse>>builder()
                        .success(true)
                        .message("Reviews fetched successfully")
                        .data(reviewService.getReviewsByBookId(bookId))
                        .build()
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewsByUserId(@PathVariable Long userId) {
        log.info("Fetching reviews for user with id: {}", userId);
        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponse>>builder()
                        .success(true)
                        .message("Reviews fetched successfully")
                        .data(reviewService.getReviewsByUserId(userId))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<ReviewResponse>> addReview(@Valid @RequestBody ReviewRequest reviewRequest) {
        log.info("Adding review");
        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .success(true)
                        .message("Review added successfully")
                        .data(reviewService.addReview(reviewRequest))
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(@PathVariable Long id,
                                                                    @Valid@RequestBody ReviewRequest reviewRequest) {
        log.info("Updating review with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .success(true)
                        .message("Review updated successfully")
                        .data(reviewService.updateReview(id, reviewRequest))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReviewById(@PathVariable Long id) {
        log.info("Deleting review with id: {}", id);
        reviewService.deleteReviewById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Review deleted successfully")
                        .build()
        );
    }
}
