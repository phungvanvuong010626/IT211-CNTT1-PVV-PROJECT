package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.it211_pvv_project.model.enums.SubmissionStatus;

import java.time.LocalDateTime;

//Bài nộp đồ án của sinh viên
@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sinh viên nộp bài
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // Khóa học
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Link github đồ án
    private String githubUrl;

    // Link file báo cáo trên Cloudinary
    private String reportUrl;

    // Ghi chú của sinh viên
    @Column(columnDefinition = "TEXT")
    private String note;

    // Điểm số
    private Integer score;

    // Nhận xét của giảng viên
    @Column(columnDefinition = "TEXT")
    private String feedback;

    // Trạng thái bài nộp
    @Enumerated(EnumType.STRING)
    private SubmissionStatus status;

    private LocalDateTime submittedAt;

    private LocalDateTime gradedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = SubmissionStatus.PENDING;
        }
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}