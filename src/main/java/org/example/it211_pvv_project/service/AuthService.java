package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.request.ChangePasswordRequest;
import org.example.it211_pvv_project.model.dto.request.LoginRequest;
import org.example.it211_pvv_project.model.dto.request.RefreshTokenRequest;
import org.example.it211_pvv_project.model.dto.request.RegisterRequest;
import org.example.it211_pvv_project.model.dto.response.LoginResponse;
import org.example.it211_pvv_project.model.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String accessToken);
    // Đổi mật khẩu
    void changePassword(
            ChangePasswordRequest request,
            String username
    );
}