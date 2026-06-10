package org.example.it211_pvv_project.exception;

// * Trạng thái nghiệp vụ không hợp lệ
// * Ví dụ:
// * Chưa nộp bài mà chấm điểm
// * Đã chấm điểm rồi nhưng tiếp tục chấm
public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }
}