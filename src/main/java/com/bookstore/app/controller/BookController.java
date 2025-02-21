package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.BookResponse;
import com.bookstore.app.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookController {

    BookService bookService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAll() {
        log.info("Get all books");
        return ResponseEntity.ok(
                ApiResponse.<List<BookResponse>>builder()
                        .success(true)
                        .message("Get all books")
                        .data(bookService.getAll())
                        .build()
        );
    }

}
