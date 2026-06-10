package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.Enrollment;
import org.example.it211_pvv_project.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//Quản lý đăng ký khóa học.
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long>{
    boolean existsByStudentAndCourse(User student, Course course);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    List<Enrollment> findByStudent(User student);
    List<Enrollment> findByCourse(Course course);
}