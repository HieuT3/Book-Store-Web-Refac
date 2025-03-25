package com.bookstore.app.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    Long userId;
    String email;
    String fullName;
    String phone;
    String address;
}
