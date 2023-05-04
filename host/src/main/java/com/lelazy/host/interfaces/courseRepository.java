package com.lelazy.host.interfaces;

import com.lelazy.host.classes.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface courseRepository extends JpaRepository<Course,Integer> {
    @Query(value = "select * from Course",nativeQuery = true)
    List<Course> findAll();
}
