package com.lelazy.host.service;

import com.lelazy.host.classes.Course;
import com.lelazy.host.interfaces.courseRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class courseservice {
    @Resource
    com.lelazy.host.interfaces.courseRepository courseRepository;
    @Transactional
    public List<Course> getcourse(){
        return courseRepository.findAll();
    }
}
