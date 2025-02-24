package com.bookstore.app.service.impl;

import com.bookstore.app.constant.RoleType;
import com.bookstore.app.dto.request.UserRequest;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    ModelMapper modelMapper;

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .toList();
    }

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

    @Override
    public UserResponse createUserWithUserRole(UserRequest userRequest) {
        return modelMapper.map(userRepository.save(setUser(userRequest, RoleType.USER)), UserResponse.class);
    }

    public UserResponse createUserWithAdminRole(UserRequest userRequest) {
        return modelMapper.map(userRepository.save(setUser(userRequest, RoleType.ADMIN)), UserResponse.class);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User userById = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + id)
        );
        User userByEmail = userRepository.findByEmail(userRequest.getEmail()).orElse(null);
        if(userByEmail != null && !Objects.equals(userById.getUserId(), userByEmail.getUserId()))
            throw new ResourceAlreadyExistsException("User already exists with email: " + userRequest.getEmail());

        userById.setEmail(userRequest.getEmail());
        userById.setFullName(userRequest.getFullName());
        userById.setPassword(userRequest.getPassword());
        userById.setPhone(userById.getPhone());
        userById.setAddress(userById.getAddress());

        return modelMapper.map(userById, UserResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.deleteById(id);
    }

    private User setUser(UserRequest userRequest, RoleType roleType) {
        if(userRepository.findByEmail(userRequest.getEmail()).isPresent())
            throw new ResourceAlreadyExistsException("User already exists with email: " + userRequest.getEmail());
        User savedUser = modelMapper.map(userRequest, User.class);
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
