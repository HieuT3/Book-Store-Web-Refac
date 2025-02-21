package com.bookstore.app.repository;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
