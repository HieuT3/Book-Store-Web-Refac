package com.bookstore.app.service.impl;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.RegisterRequest;
import com.bookstore.app.dto.response.UserResponse;
import com.bookstore.app.entity.User;
import com.bookstore.app.exception.ResourceAlreadyExistsException;
import com.bookstore.app.exception.ResourceNotFoundException;
import com.bookstore.app.repository.RoleRepository;
import com.bookstore.app.repository.UserRepository;
import com.bookstore.app.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    @Cacheable(value = "users", key = "'all'")
    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Cacheable(value = "users", key = "#id")
    @Override
    public UserResponse getUserById(Long id) {
        return modelMapper.map(
                userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)),
                UserResponse.class
        );
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return modelMapper.map(
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email)),
                UserResponse.class
        );
    }

    @CacheEvict(value = "users", key = "'all'")
    @Override
    public UserResponse createUserWithRole(RegisterRequest registerRequest, RoleType roleType) {
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        return modelMapper.map(userRepository.save(setUser(registerRequest, roleType)), UserResponse.class);
    }

    @CachePut(value = "users", key = "#id")
    @Override
    public UserResponse updateUser(Long id, RegisterRequest registerRequest) {
        User userById = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );
        User userByEmail = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
        if(userByEmail != null && !Objects.equals(userById.getUserId(), userByEmail.getUserId()))
            throw new ResourceAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());

        userById.setEmail(registerRequest.getEmail());
        userById.setFullName(registerRequest.getFullName());
        userById.setPassword(registerRequest.getPassword());
        userById.setPhone(userById.getPhone());
        userById.setAddress(userById.getAddress());

        return modelMapper.map(userById, UserResponse.class);
    }

    @CacheEvict(value = "users", key = "#id")
    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    private User setUser(RegisterRequest registerRequest, RoleType roleType) {
        if(userRepository.findByEmail(registerRequest.getEmail()).isPresent())
            throw new ResourceAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());
        User savedUser = modelMapper.map(registerRequest, User.class);
        savedUser.setRoles(
                Set.of(
                        roleRepository.findByName(roleType)
                                .orElseThrow(
                                        () -> new ResourceNotFoundException("Role not found with name: " + roleType.name())
                                )
                )
        );
        return savedUser;
    }
}
