package com.bookstore.app.service;

import com.bookstore.app.dto.request.BookRequest;
import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.dto.response.PageResponse;

import java.util.List;

public interface BookService {
    List<BookResponse>  getAll();
    PageResponse<BookResponse> getBooksPagination(int page, int limit);
    BookResponse getBookById(Long id);
    BookResponse getBookByTitle(String title);
    List<BookResponse> getBooksFeatured();
    BookResponse createBook(BookRequest bookRequest);
    BookResponse updateBook(Long id, BookRequest bookRequest);
    void deleteBook(Long id);
    List<BookResponse> getAllByAuthorId(Long authorId);
    List<BookResponse> getBooksByCategoryId(Long categoryId);
    List<BookResponse> getBooksFeaturedByCategoryId(Long categoryId, int page, int limit);
    List<BookResponse> searchByTitle(String title);
}
