package com.bookstore.app.service.impl;

import com.bookstore.app.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    Cloudinary cloudinary;

    @Override
    public ApiResponse get(String public_id) {
        try {
            return cloudinary.api().resource(public_id, ObjectUtils.emptyMap());
        } catch (Exception e) {
            log.error("Error fetching image from Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error fetching image from Cloudinary: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void upload(MultipartFile file) {
        File tempFile = null;
        try {
            String originalFileNames = file.getOriginalFilename();
            if (originalFileNames == null)
                throw new IllegalArgumentException("File name cannot be empty");
            String public_id = originalFileNames.replaceAll("\\s+", "_").replaceAll("\\.\\w+$", "");
            tempFile = File.createTempFile("upload_", public_id);
            file.transferTo(tempFile);
            Map params = ObjectUtils.asMap(
                    "public_id", public_id,
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
            );
            Map result = cloudinary.uploader().upload(tempFile, params);
            log.info(result.toString());
        } catch (Exception e) {
            log.info("Error uploading image to Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error creating temporary file: " + e.getMessage());
        }
//        finally {
//            if (tempFile != null && tempFile.isFile()) tempFile.delete();
//        }
    }

    @Override
    public void delete(String public_id) {
        try {
            System.out.println(cloudinary.api().deleteResources(List.of(public_id), ObjectUtils.emptyMap()));
        } catch (Exception e) {
            log.error("Error deleting image from Cloudinary: {}", e.getMessage());
            throw new RuntimeException("Error deleting image from Cloudinary: " + e.getMessage());
        }
    }
}
