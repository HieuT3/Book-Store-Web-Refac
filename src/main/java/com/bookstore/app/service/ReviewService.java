package com.bookstore.app.service;

import com.bookstore.app.dto.request.ReviewRequest;
import com.bookstore.app.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    List<ReviewResponse> getAll();
    ReviewResponse getReviewById(Long reviewId);
    List<ReviewResponse> getReviewsByBookId(Long bookId);
    List<ReviewResponse> getReviewsByUserId(Long userId);
    ReviewResponse addReview(ReviewRequest reviewRequest);
    ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest);
    void deleteReviewById(Long reviewId);
}
