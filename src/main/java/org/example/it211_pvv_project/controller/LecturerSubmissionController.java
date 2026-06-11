package org.example.it211_pvv_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.dto.request.GradeRequest;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.SubmissionResponse;
import org.example.it211_pvv_project.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//API giảng viên xem bài nộp và chấm điểm.
@RestController
@RequestMapping("/api/v1/lecturer/submissions")
@RequiredArgsConstructor
public class LecturerSubmissionController {
    private final SubmissionService submissionService;
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubmissionResponse>>> getAllSubmissions() {
        return ResponseEntity.ok(ApiResponse.<List<SubmissionResponse>>builder()
                .success(true)
                .message("Lấy danh sách bài nộp thành công")
                .data(submissionService.getAllSubmissions())
                .build());
    }

    @PutMapping("/grade")
    public ResponseEntity<ApiResponse<SubmissionResponse>> gradeSubmission(
            @Valid @RequestBody GradeRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.<SubmissionResponse>builder()
                .success(true)
                .message("Chấm điểm thành công")
                .data(submissionService.gradeSubmission(request))
                .build());
    }
}