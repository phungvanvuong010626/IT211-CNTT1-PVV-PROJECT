package org.example.it211_pvv_project.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.it211_pvv_project.security.principal.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 Tạo token
 Đọc token
 Kiểm tra token
 */
@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.access-token-expiration-ms}")
    private long accessTokenExpirationMs;
    @Value("${app.jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Tạo Access Token
    public String generateAccessToken(CustomUserDetails userDetails) {
        return generateToken(userDetails, accessTokenExpirationMs);
    }

    // Tạo Refresh Token
    public String generateRefreshToken(CustomUserDetails userDetails) {
        return generateToken(userDetails, refreshTokenExpirationMs);
    }

    private String generateToken(CustomUserDetails userDetails, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("userId", userDetails.getUser().getId())
                .claim("role", userDetails.getUser().getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Lấy username từ token
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    // Lấy ngày hết hạn token
    public Date getExpirationFromToken(String token) {
        return getClaims(token).getExpiration();
    }

    // Kiểm tra token hợp lệ
    public boolean isTokenValid(String token, CustomUserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationFromToken(token).before(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}