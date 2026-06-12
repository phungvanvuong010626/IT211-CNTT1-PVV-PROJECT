package org.example.it211_pvv_project.controller;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.model.dto.response.ApiResponse;
import org.example.it211_pvv_project.model.dto.response.MaterialResponse;
import org.example.it211_pvv_project.service.LectureMaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * API giảng viên upload tài liệu.
 */
@RestController
@RequestMapping("/api/v1/lecturer/materials")
@RequiredArgsConstructor
public class LecturerMaterialController {

    private final LectureMaterialService lectureMaterialService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MaterialResponse>> uploadMaterial(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam MultipartFile file,
            Authentication authentication
    ) {
        MaterialResponse data = lectureMaterialService.uploadMaterial(
                courseId, title, file, authentication.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<MaterialResponse>builder()
                        .success(true)
                        .message("Upload tài liệu thành công")
                        .data(data)
                        .build());
    }
}