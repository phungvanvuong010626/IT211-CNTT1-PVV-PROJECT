package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Thao tác dữ liệu Course
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);

    Page<Course> findByActiveTrue(Pageable pageable);

    // Tìm kiếm khóa học theo mã hoặc tên
    @Query("""
        SELECT c FROM Course c
        WHERE c.active = true
        AND (
            LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Course> searchCourses(String keyword, Pageable pageable);
}