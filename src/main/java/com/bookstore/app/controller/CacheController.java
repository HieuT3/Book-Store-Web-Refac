package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.service.CacheService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cache")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CacheController {

    CacheService cacheService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<Void>> getAllCache() {
        log.info("Get all cache");
        cacheService.getAllCache();
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Cache retrieved successfully")
                        .build()
        );
    }

    @GetMapping("/get-cache")
    public ResponseEntity<ApiResponse<Object>> getCache(
            @RequestParam("value") String value,
            @RequestParam("key") Object key
    ) {
        log.info("Get cache with value: {} and key: {}", value, key);
        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Get cache successfully")
                        .data(cacheService.getCache(value, key))
                        .build()
        );
    }

    @GetMapping("/type-cache")
    public ResponseEntity<ApiResponse<String>> getTypeCache() {
        log.info("Get type cache");
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Get type cache successfully")
                        .data(cacheService.getTypeCache())
                        .build()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Void>> printCacheStats() {
        log.info("Print cache stats");
        cacheService.printCacheStats();
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Cache stats printed successfully")
                        .build()
        );
    }
}
