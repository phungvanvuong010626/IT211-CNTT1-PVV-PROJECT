package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

//Yêu cầu cấp lại Access Token.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RefreshTokenRequest {
    @NotBlank(message = "Refresh Token không được để trống")
    private String refreshToken;
}