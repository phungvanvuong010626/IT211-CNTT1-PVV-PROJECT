package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

//Kết quả đăng nhập.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    // Loại token
    private String tokenType;
    private Long userId;
    private String username;
    private String role;
}


//        Dùng cho: POST /api/auth/login
//        Ví dụ:
//
//        {
//        "accessToken": "eyJhbGci...",
//        "refreshToken": "eyJhbGci...",
//        "tokenType": "Bearer",
//        "userId": 1,
//        "username": "admin",
//        "role": "ROLE_ADMIN"
//        }