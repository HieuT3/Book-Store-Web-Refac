package com.bookstore.app.service;

import com.bookstore.app.elasticsearch.BookDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchService {
    Page<BookDocument> search(String title,
                              String author,
                              String category,
                              int pageNumber,
                              int pageSize,
                              String order);
    Page<BookDocument> searchBooksByTitle(String title, Pageable pageable);
    Page<BookDocument> searchBooksByTitleAndAuthor(String title, String author, Pageable pageable);
    Page<BookDocument> searchBooksByTitleAndCategory(String title, String category, Pageable pageable);
    Page<BookDocument> searchBooksByAuthorAndCategory(String author, String category, Pageable pageable);
    Page<BookDocument> searchBooksByTitleAndAuthorAndCategory(String title, String author, String category, Pageable pageable);
    Page<BookDocument> filterBooksByAuthor(String author, Pageable pageable);
    Page<BookDocument> filterBooksByCategory(String category, Pageable pageable);
    Page<BookDocument> rangeBooksByPrice(double lte, double gte, Pageable pageable);
}
