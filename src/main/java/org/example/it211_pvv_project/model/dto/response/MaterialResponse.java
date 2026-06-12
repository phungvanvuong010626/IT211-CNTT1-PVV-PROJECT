package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MaterialResponse {
    private Long id;
    private String title;
    private String fileUrl;
    private String fileType;
    private String courseName;
    private String uploadedBy;
}