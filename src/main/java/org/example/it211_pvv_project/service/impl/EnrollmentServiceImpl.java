package org.example.it211_pvv_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.model.dto.response.CourseResponse;
import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.Enrollment;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.repository.CourseRepository;
import org.example.it211_pvv_project.repository.EnrollmentRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.service.EnrollmentService;
import org.springframework.stereotype.Service;

/**
 * Xử lý sinh viên đăng ký khóa học.
 */
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public CourseResponse enrollCourse(Long courseId, String username) {
        User student = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new ConflictException("Sinh viên đã đăng ký khóa học này");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);

        return toCourseResponse(course);
    }

    private CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .description(course.getDescription())
                .lecturerName(course.getLecturer() != null ? course.getLecturer().getFullName() : null)
                .active(course.getActive())
                .build();
    }
}