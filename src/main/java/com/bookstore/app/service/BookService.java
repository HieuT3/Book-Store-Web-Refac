package com.bookstore.app.service;

import com.bookstore.app.dto.request.BookRequest;
import com.bookstore.app.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse>  getAll();
    BookResponse getBookById(Long id);
    BookResponse getBookByTitle(String title);
    BookResponse createBook(BookRequest bookRequest);
    BookResponse updateBook(Long id, BookRequest bookRequest);
    void deleteBook(Long id);
    List<BookResponse> getAllByAuthorId(Long authorId);
    List<BookResponse> getAllByCategoryId(Long categoryId);
    List<BookResponse> searchByTitle(String title);
}
