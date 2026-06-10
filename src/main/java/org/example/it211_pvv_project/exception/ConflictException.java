package org.example.it211_pvv_project.exception;

//Trùng dữ liệu.
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}