package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//Danh sách token đã logout.
@Entity
@Table(name = "token_blacklist")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Access token đã bị vô hiệu hóa
    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    // Thời gian hết hạn của token
    private LocalDateTime expiryDate;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}