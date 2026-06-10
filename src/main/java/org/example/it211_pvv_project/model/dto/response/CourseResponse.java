package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

//Thông tin khóa học.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseResponse {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String lecturerName;
    private Boolean active;
}