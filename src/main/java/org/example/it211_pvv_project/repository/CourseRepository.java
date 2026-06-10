package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//Thao tác dữ liệu Course.
public interface CourseRepository extends JpaRepository<Course, Long>{
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
}