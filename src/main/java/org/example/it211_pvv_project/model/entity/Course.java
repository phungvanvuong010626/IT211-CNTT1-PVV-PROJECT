package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//Khóa học.
@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã khóa học
    @Column(nullable = false, unique = true)
    private String code;

    // Tên khóa học
    @Column(nullable = false)
    private String name;

    // Mô tả
    @Column(columnDefinition = "TEXT")
    private String description;

    // Giảng viên phụ trách
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    // Trạng thái hoạt động
    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}