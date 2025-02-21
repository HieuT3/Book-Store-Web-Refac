package com.bookstore.app.dto.request;

import com.bookstore.app.constant.RoleType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    RoleType name;
}
