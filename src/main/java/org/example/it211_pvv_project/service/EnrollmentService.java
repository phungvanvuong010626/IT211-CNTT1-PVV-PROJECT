package org.example.it211_pvv_project.service;

import org.example.it211_pvv_project.model.dto.response.CourseResponse;

public interface EnrollmentService {
    CourseResponse enrollCourse(Long courseId, String username);
}