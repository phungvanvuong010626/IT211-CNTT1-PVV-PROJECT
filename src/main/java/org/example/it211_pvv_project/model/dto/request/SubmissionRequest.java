package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

//Dữ liệu nộp bài.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubmissionRequest {
    @NotNull(message = "Course ID không được để trống")
    private Long courseId;

    private String githubUrl;
    private String note;
}