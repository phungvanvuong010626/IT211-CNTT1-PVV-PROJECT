package org.example.it211_pvv_project.exception;


// * Lỗi xác thực
// * Ví dụ:
// * Sai username/password
// * Refresh token không hợp lệ
// * Token hết hạn
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}