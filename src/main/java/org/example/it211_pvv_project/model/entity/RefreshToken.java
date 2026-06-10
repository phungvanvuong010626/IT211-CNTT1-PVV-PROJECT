package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//Refresh Token dùng để cấp lại Access Token.
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Chuỗi refresh token
    @Column(nullable = false, unique = true, length = 500)
    private String token;

    // Chủ sở hữu token
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Thời gian hết hạn
    private LocalDateTime expiryDate;

    // Đã bị thu hồi hay chưa
    private Boolean revoked;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.revoked = false;
        this.createdAt = LocalDateTime.now();
    }
}