package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.elasticsearch.BookDocument;
import com.bookstore.app.service.SearchService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    SearchService searchService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<BookDocument>>> searchBookDocuments(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "author", defaultValue = "") String author,
            @RequestParam(value = "category", defaultValue = "") String category,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "order", defaultValue = "") String order
    ) {
        log.info("Searching for books with title: {}, author: {}, category: {}, pageNumber: {}, pageSize: {}, order: {}",
                title, author, category, pageNumber, pageSize, order
        );
        return ResponseEntity.ok(
                ApiResponse.<Page<BookDocument>>builder()
                        .success(true)
                        .message("Books found")
                        .data(searchService.search(title, author, category, pageNumber, pageSize, order))
                        .build()
        );
    }
}
