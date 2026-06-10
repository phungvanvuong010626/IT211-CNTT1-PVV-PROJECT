package org.example.it211_pvv_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

//Sinh viên đăng ký khóa học.
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"student_id", "course_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sinh viên
    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    // Khóa học
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Thời điểm đăng ký
    private LocalDateTime enrolledAt;

    @PrePersist
    public void prePersist() {
        this.enrolledAt = LocalDateTime.now();
    }
}