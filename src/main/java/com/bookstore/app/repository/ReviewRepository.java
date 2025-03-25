package com.bookstore.app.repository;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Review;
import com.bookstore.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByReviewId(Long id);

    Optional<List<Review>> findByBook(Book book);

    Optional<List<Review>> findByUser(User user);
}
