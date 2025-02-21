package com.bookstore.app.service;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RoleRequest;
import com.bookstore.app.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> getAll();
    RoleResponse getRoleById(Long id);
    RoleResponse getRoleByName(RoleType name);
    RoleResponse updateRole(Long id, RoleRequest roleRequest);
    void deleteRoleById(Long id);
}
