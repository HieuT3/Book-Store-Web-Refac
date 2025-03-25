package com.bookstore.app.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CloudinaryConfig {

    Dotenv dotenv;

    @Bean
    public Cloudinary cloudinary() {
        String url = dotenv.get("CLOUDINARY_URL");
        return new Cloudinary(url);
    }
}
