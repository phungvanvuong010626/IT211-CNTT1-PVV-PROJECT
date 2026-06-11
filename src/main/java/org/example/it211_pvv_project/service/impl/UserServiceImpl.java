package org.example.it211_pvv_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.model.dto.request.UserRequest;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.Role;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Xử lý nghiệp vụ quản lý User
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getUsers(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<User> users = (keyword == null || keyword.isBlank())
                ? userRepository.findByDeletedFalse(pageRequest)
                : userRepository.findByDeletedFalseAndUsernameContainingIgnoreCaseOrDeletedFalseAndEmailContainingIgnoreCaseOrDeletedFalseAndFullNameContainingIgnoreCase(
                keyword, keyword, keyword, pageRequest);

        return users.map(this::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findUser(id);
        return toResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã tồn tại");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.valueOf(request.getRole()))
                .build();

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = findUser(id);

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRole(Role.valueOf(request.getRole()));

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        User user = findUser(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.getDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng"));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }
}