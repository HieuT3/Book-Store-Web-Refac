package com.bookstore.app.controller;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RoleRequest;
import com.bookstore.app.dto.response.ApiResponse;
import com.bookstore.app.dto.response.RoleResponse;
import com.bookstore.app.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll() {
        log.info("Get all roles");
        return ResponseEntity.ok(
                ApiResponse.<List<RoleResponse>>builder()
                        .success(true)
                        .message("Get all roles successfully!")
                        .data(this.roleService.getAll())
                        .build()
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable("id") Long id) {
        log.info("Get role by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<RoleResponse>builder()
                        .success(true)
                        .message("Get role by id: " + id + " successfully!")
                        .data(roleService.getRoleById(id))
                        .build()
        );
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleByName(@PathVariable("name") String name) {
        log.info("Get role by name: {}", name);
        RoleType roleName = RoleType.ADMIN.name().toLowerCase().equals(name) ? RoleType.ADMIN : RoleType.USER;
        return ResponseEntity.ok(
                ApiResponse.<RoleResponse>builder()
                        .success(true)
                        .message("Get role by name: " + name  + " successfully!")
                        .data(roleService.getRoleByName(roleName))
                        .build()
        );
    }

//    Khong the them moi doi voi enum
//    @PostMapping("")
//    public ResponseEntity<ApiResponse<RoleResponse>> createRole(
//            @Valid @RequestBody RoleRequest roleRequest) {
//        return ResponseEntity.ok(
//                ApiResponse.<RoleResponse>builder()
//                        .success(true)
//                        .message("Role created successfully")
//                        .data(roleService.createRole(roleRequest))
//                        .build()
//        );
//    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable("id") Long id,
            @Valid @RequestBody RoleRequest roleRequest
    ) {
        log.info("Update role by id: {}", id);
        return ResponseEntity.ok(
                ApiResponse.<RoleResponse>builder()
                        .success(true)
                        .message("Role updated successfully")
                        .data(roleService.updateRole(id, roleRequest))
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable("id") Long id) {
        log.info("Delete role by id: {}", id);
        roleService.deleteRoleById(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Role deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
