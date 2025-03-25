package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.elasticsearch.BookDocument;
import com.bookstore.app.service.BookDocumentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/book-document")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BookDocumentController {
    BookDocumentService bookDocumentService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<BookDocument>>> getAll() {
        log.info("Fetching all book documents");
        return ResponseEntity.ok(
                ApiResponse.<List<BookDocument>>builder()
                        .success(true)
                        .message("Book documents fetched successfully")
                        .data(bookDocumentService.getAll())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDocument>> getBookDocumentById(
            @PathVariable("id") String id
    ) {
        log.info("Fetching book document with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<BookDocument>builder()
                        .success(true)
                        .message("Book document fetched successfully")
                        .data(bookDocumentService.getBookDocumentById(id))
                        .build()
        );
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<BookDocument>> addBookDocument(
            @RequestBody BookDocument bookDocument
    ) {
        log.info("Adding new book document");
        return ResponseEntity.ok(
                ApiResponse.<BookDocument>builder()
                        .success(true)
                        .message("Book document added successfully")
                        .data(bookDocumentService.addBookDocument(bookDocument))
                        .build()
        );
    }

    @PostMapping("save-all")
    public ResponseEntity<ApiResponse<List<BookDocument>>> saveAll() {
      log.info("Saving all book documents");
      return ResponseEntity.ok(
              ApiResponse.<List<BookDocument>>builder()
                      .success(true)
                      .message("Book documents saved successfully")
                      .data(bookDocumentService.saveAll())
                      .build()
      );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookDocument>> updateBookDocument(
            @PathVariable("id") String id,
            @RequestBody BookDocument bookDocument
    ) {
        log.info("Updating book document with id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<BookDocument>builder()
                        .success(true)
                        .message("Book document updated successfully")
                        .data(bookDocumentService.updateBookDocument(id, bookDocument))
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBookDocumentById(
            @PathVariable("id") String id
    ) {
        log.info("Deleting book document with id: {}", id);
        bookDocumentService.deleteBookDocumentById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Book document deleted successfully")
                        .data(null)
                        .build()
        );
    }

    @DeleteMapping("delete-all")
    public ResponseEntity<ApiResponse<Void>> deleteAll() {
        log.info("Deleting all book documents");
        bookDocumentService.deleteAll();
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Book documents deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
