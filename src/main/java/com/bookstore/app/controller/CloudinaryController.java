package com.bookstore.app.controller;

import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.service.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/cloudinary")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CloudinaryController {

    CloudinaryService cloudinaryService;

    @GetMapping("/{public_id}")
    public ResponseEntity<ApiResponse<com.cloudinary.api.ApiResponse>> getImage(@PathVariable String public_id) {
        log.info("Fetching image with public_id: {}", public_id);
        return ResponseEntity.ok(
                ApiResponse.<com.cloudinary.api.ApiResponse>builder()
                        .success(true)
                        .message("Image fetched successfully")
                        .data(cloudinaryService.get(public_id))
                        .build()
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Void>> upload(@RequestParam("file")MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        cloudinaryService.upload(file);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("File uploaded successfully")
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        log.info("Deleting image with public_id: {}", id);
        cloudinaryService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Image deleted successfully")
                        .build()
        );
    }
}
