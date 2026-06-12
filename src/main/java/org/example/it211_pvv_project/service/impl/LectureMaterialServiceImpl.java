package org.example.it211_pvv_project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.model.dto.response.MaterialResponse;
import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.LectureMaterial;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.repository.CourseRepository;
import org.example.it211_pvv_project.repository.LectureMaterialRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.service.LectureMaterialService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

//Xử lý upload tài liệu bài giảng
@Service
@RequiredArgsConstructor
public class LectureMaterialServiceImpl implements LectureMaterialService {

    private final Cloudinary cloudinary;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final LectureMaterialRepository lectureMaterialRepository;

    @Override
    public MaterialResponse uploadMaterial(Long courseId, String title, MultipartFile file, String username) {
        validateFile(file);

        Course course = courseRepository.findById(courseId)
                .filter(Course::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        User lecturer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));

        String fileUrl = uploadToCloudinary(file);

        LectureMaterial material = LectureMaterial.builder()
                .title(title)
                .fileUrl(fileUrl)
                .fileType(file.getContentType())
                .course(course)
                .uploadedBy(lecturer)
                .build();

        return toResponse(lectureMaterialRepository.save(material));
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống");
        }

        String type = file.getContentType();
        boolean validType = "application/pdf".equals(type)
                || "application/msword".equals(type)
                || "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equals(type);

        if (!validType) {
            throw new IllegalArgumentException("Chỉ cho phép file PDF, DOC, DOCX");
        }
    }

    private String uploadToCloudinary(MultipartFile file) {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "lecture-materials"
            ));

            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Upload file thất bại");
        }
    }

    private MaterialResponse toResponse(LectureMaterial material) {
        return MaterialResponse.builder()
                .id(material.getId())
                .title(material.getTitle())
                .fileUrl(material.getFileUrl())
                .fileType(material.getFileType())
                .courseName(material.getCourse().getName())
                .uploadedBy(material.getUploadedBy().getFullName())
                .build();
    }
}