package com.katarzynasurtel.kursownia.repositories;

import com.katarzynasurtel.kursownia.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {}
