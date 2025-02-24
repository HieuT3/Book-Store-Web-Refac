package com.bookstore.app.dto.response;

import com.bookstore.app.constant.RoleType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Long roleId;
    RoleType name;
    String description;
}
