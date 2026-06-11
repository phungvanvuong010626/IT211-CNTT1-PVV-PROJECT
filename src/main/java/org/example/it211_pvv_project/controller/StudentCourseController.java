package org.example.it211_pvv_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.CourseResponse;
import org.example.it211_pvv_project.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

//API sinh viên đăng ký khóa học.
@RestController
@RequestMapping("/api/v1/student/courses")
@RequiredArgsConstructor
public class StudentCourseController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<ApiResponse<CourseResponse>> enrollCourse(
            @PathVariable Long courseId,
            Authentication authentication
    ) {
        CourseResponse data = enrollmentService.enrollCourse(courseId, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<CourseResponse>builder()
                        .success(true)
                        .message("Đăng ký khóa học thành công")
                        .data(data)
                        .build());
    }
}