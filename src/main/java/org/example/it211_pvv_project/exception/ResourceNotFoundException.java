package org.example.it211_pvv_project.exception;

//Không tìm thấy dữ liệu
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}