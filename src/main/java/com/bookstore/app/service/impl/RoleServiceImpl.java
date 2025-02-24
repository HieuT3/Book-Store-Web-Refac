package com.bookstore.app.service.impl;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RoleRequest;
import com.bookstore.app.dto.response.RoleResponse;
import com.bookstore.app.entity.Role;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.RoleRepository;
import com.bookstore.app.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    ModelMapper modelMapper;

    @Override
    public List<RoleResponse> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .toList();
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        return roleRepository.findById(id)
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .orElseThrow(
                        () -> new ResourceNotFoundException("Role not found")
                );
    }

    @Override
    public RoleResponse getRoleByName(RoleType name) {
        return roleRepository.findByName(name)
                .map(role -> modelMapper.map(role, RoleResponse.class))
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

//    @Override
//    public RoleResponse createRole(RoleRequest roleRequest) {
//        roleRepository.findByName(roleRequest.getName())
//                .ifPresent(
//                        role -> {
//                            throw new ResourceAlreadyExistsException("Role already exists");
//                        }
//                );
//        Role savedRole = modelMapper.map(roleRequest, Role.class);
//        return modelMapper.map(roleRepository.save(savedRole), RoleResponse.class);
//    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        existingRole.setName(roleRequest.getName());
        return modelMapper.map(roleRepository.save(existingRole), RoleResponse.class);
    }

    @Override
    public void deleteRoleById(Long id) {
        roleRepository.findById(id)
                .ifPresentOrElse(
                        (role) -> roleRepository.deleteById(id),
                        () -> {
                            throw new ResourceNotFoundException("Role not found");
                        }
                );
    }
}
