package com.bookstore.app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenResponse {
    String accessToken;
    long expiration;
    UserResponse user;
}
