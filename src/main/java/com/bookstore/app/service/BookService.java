package com.bookstore.app.service;

import com.bookstore.app.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse>  getAll();
//    BookResponse getAuthorById(Long id);
//    BookResponse create(BookRequest request);
//    BookResponse update(Long id, BookRequest request);
//    void delete(Long id);
//    List<BookResponse> getAllByAuthorId(Long authorId);
//    List<BookResponse> getAllByCategory(String category);
//    List<BookResponse> searchByTitle(String title);
}
