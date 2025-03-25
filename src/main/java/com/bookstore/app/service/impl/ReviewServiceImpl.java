package com.bookstore.app.service.impl;

import com.bookstore.app.dto.request.ReviewRequest;
import com.bookstore.app.dto.response.ReviewResponse;
import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Review;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.repository.ReviewRepository;
import com.bookstore.app.repository.UserRepository;
import com.bookstore.app.security.CustomUserDetails;
import com.bookstore.app.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    ModelMapper modelMapper;
    BookRepository bookRepository;
    UserRepository userRepository;


    @Override
    public List<ReviewResponse> getAll() {
        return reviewRepository.findAll()
                .stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .toList();
    }

    @Override
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with reviewId: " + reviewId));
        return modelMapper.map(review, ReviewResponse.class);
    }

    @Override
    public List<ReviewResponse> getReviewsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        List<Review> reviews = reviewRepository.findByBook(book)
                .orElseThrow(() -> new ResourceNotFoundException("No reviews found for book with id: " + bookId));
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .toList();
    }

    @Override
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Review> reviews = reviewRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No reviews found for user with id: " + userId));
        return reviews.stream()
                .map(review -> modelMapper.map(review, ReviewResponse.class))
                .toList();
    }

    @Override
    public ReviewResponse addReview(ReviewRequest reviewRequest) {
        Book book = bookRepository.findById(reviewRequest.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + reviewRequest.getBookId()));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setComment(review.getComment());

        return modelMapper.map(reviewRepository.save(review), ReviewResponse.class);
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, ReviewRequest reviewRequest) {
        Review exsistingReview = reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with reviewId: " + reviewId));
        exsistingReview.setComment(reviewRequest.getComment());
        return modelMapper.map(reviewRepository.save(exsistingReview), ReviewResponse.class);
    }

    @Override
    public void deleteReviewById(Long reviewId) {
        reviewRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with reviewId: " + reviewId));
        reviewRepository.deleteById(reviewId);
    }
}
