package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.Submission;
import org.example.it211_pvv_project.model.entity.User;
import org.example.it211_pvv_project.model.enums.SubmissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Quản lý bài nộp.
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent(User student);
    List<Submission> findByCourse(Course course);
    List<Submission> findByStatus(SubmissionStatus status);
    Optional<Submission> findByStudentAndCourse(User student, Course course);
}