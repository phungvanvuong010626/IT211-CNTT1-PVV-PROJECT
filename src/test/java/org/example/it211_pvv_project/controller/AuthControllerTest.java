package org.example.it211_pvv_project.controller;

import org.example.it211_pvv_project.model.dto.request.*;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.LoginResponse;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.example.it211_pvv_project.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    // Test đăng ký tài khoản
    @Test
    void shouldRegisterSuccessfully() {
        RegisterRequest request = RegisterRequest.builder()
                .username("student1")
                .email("student1@gmail.com")
                .password("123456")
                .fullName("Nguyen Van A")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .username("student1")
                .email("student1@gmail.com")
                .fullName("Nguyen Van A")
                .role("ROLE_STUDENT")
                .status("ACTIVE")
                .build();

        when(authService.register(request)).thenReturn(userResponse);

        ResponseEntity<ApiResponse<UserResponse>> response =
                authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("student1", response.getBody().getData().getUsername());
    }

    // Test đăng nhập
    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = LoginRequest.builder()
                .username("student1")
                .password("123456")
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .username("student1")
                .role("ROLE_STUDENT")
                .build();

        when(authService.login(request)).thenReturn(loginResponse);

        ResponseEntity<ApiResponse<LoginResponse>> response =
                authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("access-token", response.getBody().getData().getAccessToken());
    }

    // Test refresh token
    @Test
    void shouldRefreshTokenSuccessfully() {
        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken("refresh-token")
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken("new-access-token")
                .refreshToken("refresh-token")
                .tokenType("Bearer")
                .build();

        when(authService.refreshToken(request)).thenReturn(loginResponse);

        ResponseEntity<ApiResponse<LoginResponse>> response =
                authController.refreshToken(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        assertEquals("new-access-token", response.getBody().getData().getAccessToken());
    }

    // Test logout
    @Test
    void shouldLogoutSuccessfully() {
        String header = "Bearer access-token";

        doNothing().when(authService).logout("access-token");

        ResponseEntity<ApiResponse<Void>> response =
                authController.logout(header);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(authService, times(1)).logout("access-token");
    }

    // Test đổi mật khẩu
    @Test
    void shouldChangePasswordSuccessfully() {
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .currentPassword("123456")
                .newPassword("654321")
                .confirmPassword("654321")
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("student1");

        doNothing().when(authService).changePassword(request, "student1");

        ResponseEntity<ApiResponse<Void>> response =
                authController.changePassword(request, authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isSuccess());
        verify(authService, times(1)).changePassword(request, "student1");
    }
}