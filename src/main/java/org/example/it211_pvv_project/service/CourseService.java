package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.request.CourseRequest;
import org.example.it211_pvv_project.model.dto.response.CourseResponse;
import org.springframework.data.domain.Page;

public interface CourseService {

    Page<CourseResponse> getCourses(String keyword, int page, int size);

    CourseResponse getCourseById(Long id);

    CourseResponse createCourse(CourseRequest request);

    CourseResponse updateCourse(Long id, CourseRequest request);

    void deleteCourse(Long id);
}