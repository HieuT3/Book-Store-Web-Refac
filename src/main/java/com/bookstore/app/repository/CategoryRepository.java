package com.bookstore.app.repository;

import com.bookstore.app.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM CATEGORIES ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Category> findCategoriesFeatured();
}
