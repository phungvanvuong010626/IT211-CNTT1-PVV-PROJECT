package org.example.it211_pvv_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.it211_pvv_project.exception.ConflictException;
import org.example.it211_pvv_project.exception.ResourceNotFoundException;
import org.example.it211_pvv_project.model.dto.request.CourseRequest;
import org.example.it211_pvv_project.model.dto.response.CourseResponse;
import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.Role;
import org.example.it211_pvv_project.repository.CourseRepository;
import org.example.it211_pvv_project.repository.UserRepository;
import org.example.it211_pvv_project.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

//Xử lý nghiệp vụ quản lý khóa học
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    // Lấy danh sách khóa học có tìm kiếm + phân trang
    @Override
    public Page<CourseResponse> getCourses(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Course> courses = (keyword == null || keyword.isBlank())
                ? courseRepository.findByActiveTrue(pageRequest)
                : courseRepository.searchCourses(keyword, pageRequest);
        return courses.map(this::toResponse);
    }

    // Lấy chi tiết khóa học
    @Override
    public CourseResponse getCourseById(Long id) {
        return toResponse(findCourse(id));
    }

    // Tạo khóa học
    @Override
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new ConflictException("Mã khóa học đã tồn tại");
        }

        User lecturer = null;
        if (request.getLecturerId() != null) {
            lecturer = userRepository.findById(request.getLecturerId())
                    .filter(user -> user.getRole() == Role.ROLE_LECTURER)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
        }

        Course course = Course.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .lecturer(lecturer)
                .active(true)
                .build();

        return toResponse(courseRepository.save(course));
    }

    // Cập nhật khóa học
    @Override
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = findCourse(id);

        course.setCode(request.getCode());
        course.setName(request.getName());
        course.setDescription(request.getDescription());

        if (request.getLecturerId() != null) {
            User lecturer = userRepository.findById(request.getLecturerId())
                    .filter(user -> user.getRole() == Role.ROLE_LECTURER)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giảng viên"));
            course.setLecturer(lecturer);
        }

        return toResponse(courseRepository.save(course));
    }

    // Xóa mềm khóa học
    @Override
    public void deleteCourse(Long id) {
        Course course = findCourse(id);
        course.setActive(false);
        courseRepository.save(course);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .filter(Course::getActive)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khóa học"));
    }

    private CourseResponse toResponse(Course course) {
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