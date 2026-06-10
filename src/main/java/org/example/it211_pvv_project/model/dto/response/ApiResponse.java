package org.example.it211_pvv_project.model.dto.response;

import lombok.*;

//Response chuẩn cho toàn bộ API.
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse<T> {
    // Kết quả xử lý
    private boolean success;
    // Thông báo
    private String message;
    // Dữ liệu trả về
    private T data;
}

//        Toàn bộ API sau này sẽ trả theo format:
//        {
//        "success": true,
//        "message": "Login successful",
//        "data": {}
//        }