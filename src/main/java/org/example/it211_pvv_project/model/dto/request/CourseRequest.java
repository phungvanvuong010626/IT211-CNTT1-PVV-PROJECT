package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

//Dữ liệu khóa học.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseRequest {
    @NotBlank(message = "Mã khóa học không được để trống")
    private String code;

    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;
    private String description;
    private Long lecturerId;
}