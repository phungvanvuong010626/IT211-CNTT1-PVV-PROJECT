package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

//Dữ liệu chấm điểm.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GradeRequest {
    @NotNull(message = "Submission ID không được để trống")
    private Long submissionId;

    @NotNull(message = "Điểm không được để trống")
    @Min(value = 0, message = "Điểm tối thiểu là 0")
    @Max(value = 100, message = "Điểm tối đa là 100")
    private Integer score;

    private String feedback;
}