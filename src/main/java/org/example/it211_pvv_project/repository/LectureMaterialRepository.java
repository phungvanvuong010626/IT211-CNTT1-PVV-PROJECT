package org.example.it211_pvv_project.repository;

import org.example.it211_pvv_project.model.entity.Course;
import org.example.it211_pvv_project.model.entity.LectureMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Quản lý tài liệu học tập.
public interface LectureMaterialRepository extends JpaRepository<LectureMaterial, Long> {
    List<LectureMaterial> findByCourse(Course course);
}