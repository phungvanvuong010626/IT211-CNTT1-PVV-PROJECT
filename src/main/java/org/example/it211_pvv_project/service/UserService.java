package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.request.UserRequest;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.springframework.data.domain.Page;
//Khai báo các chức năng quản lý User.
public interface UserService {
    // Danh sách User có tìm kiếm + phân trang
    Page<UserResponse> getUsers(String keyword, int page, int size);
    UserResponse getUserById(Long id);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
}