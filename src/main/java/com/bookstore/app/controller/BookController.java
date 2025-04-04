package com.bookstore.app.controller;

import com.bookstore.app.dto.request.BookRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.dto.response.PageResponse;
import com.bookstore.app.repository.BookRepository;
import com.bookstore.app.service.BookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookController {

    BookService bookService;
    BookRepository bookRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAll() {
        log.info("Get all books");
        return ResponseEntity.ok(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .message("Get all books successfully!")
                        .data(bookService.getAll())
                        .build()
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable("id") Long id) {
        log.info("Get book by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .message("Get book by id: " + id + " successfully!")
                        .data(bookService.getBookById(id))
                        .build()
        );
    }

    @GetMapping("/pagination")
    public ResponseEntity<ApiResponse<PageResponse<BookResponse>>> getBooksPagination(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        log.info("Get books pagination with page: {} and limit: {}", page, limit);
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<BookResponse>>builder()
                        .success(true)
                        .message("Get books pagination with page: " + page + " and limit: " + limit + " successfully!")
                        .data(bookService.getBooksPagination(page, limit))
                        .build()
        );
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksByCategoryId(@PathVariable("id") Long categoryId) {
        log.info("Get books by category id: {}", categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .message("Get books by category id: " + categoryId + " successfully!")
                        .data(bookService.getBooksByCategoryId(categoryId))
                        .build()
        );
    }

    @GetMapping("featured/category/{id}")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooksFeaturedByCategoryId(
            @PathVariable("id") Long categoryId,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        log.info("Get books featured by category id: {}", categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .message("Get books featured by category id: " + categoryId + " successfully!")
                        .data(bookService.getBooksFeaturedByCategoryId(categoryId, page, limit))
                        .build()
        );
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookByTitle(@PathVariable("title") String title) {
        log.info("Get book by title: {}", title);
        return ResponseEntity.ok(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .message("Get book by title: " + title + " successfully!")
                        .data(bookService.getBookByTitle(title))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(
            @Valid @RequestBody BookRequest bookRequest
            ) {
        log.info("Create book with title: {}", bookRequest.getTitle());
        return ResponseEntity.ok(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .message("Create book with title: " + bookRequest.getTitle() + " successfully!")
                        .data(bookService.createBook(bookRequest))
                        .build()
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable("id") Long id,
            @Valid @RequestBody BookRequest bookRequest
    ) {
        log.info("Update book with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<BookResponse>builder()
                        .success(true)
                        .message("Update book with id: " + id + " successfully!")
                        .data(bookService.updateBook(id, bookRequest))
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") Long id) {
        log.info("Delete book with id: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Delete book with id: " + id + " successfully!")
                        .data(null)
                        .build()
        );
    }

//    @PutMapping("stock")
//    public ResponseEntity<ApiResponse<Void>> updateBookStock(
//            @RequestParam("quantity") int quantity
//    ) {
//        log.info("Update book stock with quantity: {}", quantity);
//        List<Book> books = bookRepository.findAll();
//        books.forEach(book -> book.setStock(quantity));
//        bookRepository.saveAll(books);
//        return ResponseEntity.ok(
//                ApiResponse.<Void>builder()
//                        .success(true)
//                        .data(null)
//                        .build()
//        );
//    }
}
