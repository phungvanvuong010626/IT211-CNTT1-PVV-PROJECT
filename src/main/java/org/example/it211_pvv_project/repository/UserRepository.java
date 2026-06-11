package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Thao tác dữ liệu User
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    // Kiểm tra username đã tồn tại chưa
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    // Tìm user theo role
    List<User> findByRole(Role role);
    // Lấy danh sách user chưa bị xóa mềm
    Page<User> findByDeletedFalse(Pageable pageable);
    // Tìm kiếm user theo username, email hoặc họ tên
    Page<User> findByDeletedFalseAndUsernameContainingIgnoreCaseOrDeletedFalseAndEmailContainingIgnoreCaseOrDeletedFalseAndFullNameContainingIgnoreCase(
            String username,
            String email,
            String fullName,
            Pageable pageable
    );
}