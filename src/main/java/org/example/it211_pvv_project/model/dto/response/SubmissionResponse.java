package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubmissionResponse {
    private Long id;
    private String studentName;
    private String courseName;
    private String githubUrl;
    private String reportUrl;
    private String note;
    private Integer score;
    private String feedback;
    private String status;
}