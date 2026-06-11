package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.Submission;
import org.example.it211_pvv_project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Thao tác dữ liệu Submission
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    // Danh sách bài nộp của sinh viên
    List<Submission> findByStudent(User student);
    // Danh sách bài nộp theo khóa học
    List<Submission> findByCourse(Course course);
    // Kiểm tra sinh viên đã nộp bài cho khóa học chưa
    boolean existsByStudentAndCourse(User student, Course course);
    // Tìm bài nộp của sinh viên trong khóa học
    Optional<Submission> findByStudentAndCourse(User student, Course course);
}