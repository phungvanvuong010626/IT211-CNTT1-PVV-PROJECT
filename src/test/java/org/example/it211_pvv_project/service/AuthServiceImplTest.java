package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.model.dto.request.LoginRequest;
import org.example.it211_pvv_project.model.dto.request.RefreshTokenRequest;
import org.example.it211_pvv_project.model.dto.request.RegisterRequest;
import org.example.it211_pvv_project.model.dto.response.LoginResponse;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.example.it211_pvv_project.model.entity.RefreshToken;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.Role;
import org.example.it211_pvv_project.model.enums.UserStatus;
import org.example.it211_pvv_project.repository.RefreshTokenRepository;
import org.example.it211_pvv_project.repository.TokenBlacklistRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.security.jwt.JwtService;
import org.example.it211_pvv_project.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private TokenBlacklistRepository tokenBlacklistRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    // Test đăng ký thành công
    @Test
    void shouldRegisterSuccessfully() {
        RegisterRequest request = RegisterRequest.builder()
                .username("student1")
                .email("student1@gmail.com")
                .password("123456")
                .fullName("Nguyen Van A")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .username("student1")
                .email("student1@gmail.com")
                .password("encoded")
                .fullName("Nguyen Van A")
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .deleted(false)
                .build();

        when(userRepository.existsByUsername("student1")).thenReturn(false);
        when(userRepository.existsByEmail("student1@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = authService.register(request);

        assertEquals("student1", response.getUsername());
        assertEquals("ROLE_STUDENT", response.getRole());
    }

    // Test đăng ký trùng username
    @Test
    void shouldThrowExceptionWhenUsernameExists() {
        RegisterRequest request = RegisterRequest.builder()
                .username("student1")
                .email("student1@gmail.com")
                .password("123456")
                .fullName("Nguyen Van A")
                .build();

        when(userRepository.existsByUsername("student1")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
    }

    // Test đăng nhập thành công
    @Test
    void shouldLoginSuccessfully() {
        LoginRequest request = LoginRequest.builder()
                .username("student1")
                .password("123456")
                .build();

        User user = User.builder()
                .id(1L)
                .username("student1")
                .email("student1@gmail.com")
                .password("encoded")
                .fullName("Nguyen Van A")
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .deleted(false)
                .build();

        when(userRepository.findByUsername("student1")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        LoginResponse response = authService.login(request);

        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
    }

    // Test refresh token thành công
    @Test
    void shouldRefreshTokenSuccessfully() {
        User user = User.builder()
                .id(1L)
                .username("student1")
                .role(Role.ROLE_STUDENT)
                .status(UserStatus.ACTIVE)
                .deleted(false)
                .build();

        RefreshToken token = RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        RefreshTokenRequest request = RefreshTokenRequest.builder()
                .refreshToken("refresh-token")
                .build();

        when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(Optional.of(token));
        when(jwtService.generateAccessToken(any())).thenReturn("new-access-token");

        LoginResponse response = authService.refreshToken(request);

        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    // Test gọi logout không lỗi
    @Test
    void shouldLogoutSuccessfully() {
        when(jwtService.getExpirationFromToken("access-token"))
                .thenReturn(new java.util.Date(System.currentTimeMillis() + 100000));

        assertDoesNotThrow(() -> authService.logout("access-token"));
        verify(tokenBlacklistRepository, times(1)).save(any());
    }
}