package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.it211_pvv_project.model.enums.Role;
import org.example.it211_pvv_project.model.enums.UserStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên đăng nhập
    @Column(nullable = false, unique = true)
    private String username;

    // Email
    @Column(nullable = false, unique = true)
    private String email;

    // Mật khẩu đã mã hóa BCrypt
    @Column(nullable = false)
    private String password;

    // Họ tên
    @Column(nullable = false)
    private String fullName;

    // Vai trò người dùng
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Trạng thái tài khoản
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    // Xóa mềm
    @Column(nullable = false)
    private Boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.deleted = false;
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}