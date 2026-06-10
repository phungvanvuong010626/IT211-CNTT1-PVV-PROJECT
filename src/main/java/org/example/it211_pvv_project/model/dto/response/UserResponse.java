package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

//Thông tin người dùng trả về cho client.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String status;
}