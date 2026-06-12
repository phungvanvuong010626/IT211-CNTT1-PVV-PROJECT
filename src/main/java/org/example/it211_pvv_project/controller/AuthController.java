package org.example.it211_pvv_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.dto.request.ChangePasswordRequest;
import org.example.it211_pvv_project.model.dto.request.LoginRequest;
import org.example.it211_pvv_project.model.dto.request.RefreshTokenRequest;
import org.example.it211_pvv_project.model.dto.request.RegisterRequest;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.LoginResponse;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.example.it211_pvv_project.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

//xác thực người dùng.
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        UserResponse data = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .success(true)
                        .message("Đăng ký thành công")
                        .data(data)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse data = authService.login(request);
        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Đăng nhập thành công")
                        .data(data)
                        .build()
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        LoginResponse data = authService.refreshToken(request);
        return ResponseEntity.ok(
                ApiResponse.<LoginResponse>builder()
                        .success(true)
                        .message("Cấp lại Access Token thành công")
                        .data(data)
                        .build()
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        String accessToken = authorizationHeader.replace("Bearer ", "");
        authService.logout(accessToken);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Đăng xuất thành công")
                        .data(null)
                        .build()
        );
    }


    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {

        authService.changePassword(
                request,
                authentication.getName()
        );

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Đổi mật khẩu thành công")
                        .data(null)
                        .build()
        );
    }
}



//        Test nhanh trên Postman
//        Register
//        POST http://localhost:8080/api/auth/register
//        {
//        "username": "student1",
//        "email": "student1@gmail.com",
//        "password": "123456",
//        "fullName": "Nguyen Van A"
//        }
//        Login
//        POST http://localhost:8080/api/auth/login
//        {
//        "username": "student1",
//        "password": "123456"
//        }
//
//        Kết quả mong muốn: trả về accessToken và refreshToken