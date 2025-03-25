package com.bookstore.app.dto.response;

import com.bookstore.app.constant.RoleType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long roleId;
    RoleType name;
    String description;
}
