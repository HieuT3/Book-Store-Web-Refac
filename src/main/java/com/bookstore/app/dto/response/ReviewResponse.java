package com.bookstore.app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    Long reviewId;
    BookResponse bookResponse;
    UserResponse userResponse;
    String comment;
    LocalDateTime reviewTime;
}
