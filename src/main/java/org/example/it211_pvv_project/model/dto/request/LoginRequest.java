package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

//Dữ liệu đăng nhập.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;
    @NotBlank(message = "Password không được để trống")
    private String password;
}