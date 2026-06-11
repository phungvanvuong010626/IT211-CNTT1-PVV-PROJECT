package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubmissionRequest {
    // Khóa học cần nộp bài
    @NotNull(message = "Course ID không được để trống")
    private Long courseId;
    // Link GitHub đồ án
    private String githubUrl;
    // Ghi chú của sinh viên
    private String note;
}