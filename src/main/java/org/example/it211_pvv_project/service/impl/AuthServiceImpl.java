package org.example.it211_pvv_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.exception.UnauthorizedException;
import org.example.it211_pvv_project.model.dto.request.ChangePasswordRequest;
import org.example.it211_pvv_project.model.dto.request.LoginRequest;
import org.example.it211_pvv_project.model.dto.request.RefreshTokenRequest;
import org.example.it211_pvv_project.model.dto.request.RegisterRequest;
import org.example.it211_pvv_project.model.dto.response.LoginResponse;
import org.example.it211_pvv_project.model.dto.response.UserResponse;
import org.example.it211_pvv_project.model.entity.RefreshToken;
import org.example.it211_pvv_project.model.entity.TokenBlacklist;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.Role;
import org.example.it211_pvv_project.repository.RefreshTokenRepository;
import org.example.it211_pvv_project.repository.TokenBlacklistRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.security.jwt.JwtService;
import org.example.it211_pvv_project.security.principal.CustomUserDetails;
import org.example.it211_pvv_project.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

//Xử lý đăng ký, đăng nhập, refresh token, logout.
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Username đã tồn tại");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email đã tồn tại");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ROLE_STUDENT)
                .build();

        User savedUser = userRepository.save(user);

        return toUserResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Tài khoản không tồn tại"));

        CustomUserDetails userDetails = new CustomUserDetails(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        saveRefreshToken(user, refreshToken);

        return buildLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Refresh Token không hợp lệ"));

        if (refreshToken.getRevoked()) {
            throw new UnauthorizedException("Refresh Token đã bị thu hồi");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh Token đã hết hạn");
        }

        User user = refreshToken.getUser();
        CustomUserDetails userDetails = new CustomUserDetails(user);

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return buildLoginResponse(user, newAccessToken, refreshToken.getToken());
    }

    @Override
    public void logout(String accessToken) {

        LocalDateTime expiryDate = jwtService.getExpirationFromToken(accessToken)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        TokenBlacklist blacklist = TokenBlacklist.builder()
                .token(accessToken)
                .expiryDate(expiryDate)
                .build();

        tokenBlacklistRepository.save(blacklist);
    }

    private void saveRefreshToken(User user, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusNanos(refreshTokenExpirationMs * 1_000_000))
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    private LoginResponse buildLoginResponse(User user, String accessToken, String refreshToken) {

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }

    @Override
    public void changePassword(
            ChangePasswordRequest request,
            String username
    ) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy người dùng"
                        ));

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {
            throw new UnauthorizedException(
                    "Mật khẩu hiện tại không đúng"
            );
        }
        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {
            throw new ConflictException(
                    "Xác nhận mật khẩu không khớp"
            );
        }
        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );
        userRepository.save(user);
    }
}