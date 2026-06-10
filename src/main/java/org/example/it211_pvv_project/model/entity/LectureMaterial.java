package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//Tài liệu học tập do giảng viên upload.
@Entity
@Table(name = "lecture_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tiêu đề tài liệu
    @Column(nullable = false)
    private String title;

    // Link file trên Cloudinary
    private String fileUrl;

    // Loại file (pdf, docx...)
    private String fileType;

    // Thuộc khóa học nào
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Giảng viên upload
    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    private LocalDateTime uploadedAt;

    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }
}