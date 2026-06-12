package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.response.MaterialResponse;
import org.springframework.web.multipart.MultipartFile;

public interface LectureMaterialService {
    MaterialResponse uploadMaterial(Long courseId, String title, MultipartFile file, String username);
}