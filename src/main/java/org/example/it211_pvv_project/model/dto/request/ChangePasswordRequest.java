package org.example.it211_pvv_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO đổi mật khẩu.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChangePasswordRequest {

    // Mật khẩu hiện tại
    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    private String currentPassword;

    // Mật khẩu mới
    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    // Xác nhận mật khẩu mới
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;
}