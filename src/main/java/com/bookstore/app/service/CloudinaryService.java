package com.bookstore.app.service;

import com.cloudinary.api.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {
    ApiResponse get(String public_id);
    void upload(MultipartFile file);
    void delete(String public_id);
}
