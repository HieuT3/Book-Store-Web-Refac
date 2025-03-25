package com.bookstore.app.repository;

import com.bookstore.app.entity.Book;
import com.bookstore.app.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
    List<Book> findByCategories(Set<Category> categories);

    @Query(value = "select b from Book b " +
            "join b.categories c " +
            "where c.categoryId = :categoryId")
    List<Book> findBooksFeaturedByCategory(@Param("categoryId") Long categoryId, Pageable pageable);
}
