package org.example.it211_pvv_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.dto.request.SubmissionRequest;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.SubmissionResponse;
import org.example.it211_pvv_project.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//API sinh viên nộp bài.
@RestController
@RequestMapping("/api/v1/student/submissions")
@RequiredArgsConstructor
public class StudentSubmissionController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubmissionResponse>> submitProject(
            @Valid @RequestBody SubmissionRequest request,
            Authentication authentication
    ) {
        SubmissionResponse data = submissionService.submitProject(request, authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<SubmissionResponse>builder()
                        .success(true)
                        .message("Nộp bài thành công")
                        .data(data)
                        .build());
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getMySubmissions(
            Authentication authentication
    ) {
        return ResponseEntity.ok(ApiResponse.<List<SubmissionResponse>>builder()
                .success(true)
                .message("Lấy danh sách bài nộp thành công")
                .data(submissionService.getMySubmissions(authentication.getName()))
                .build());
    }
}