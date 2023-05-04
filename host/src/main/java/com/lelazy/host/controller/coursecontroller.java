package com.lelazy.host.controller;

import com.lelazy.host.classes.Course;
import com.lelazy.host.service.courseservice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/course")
public class coursecontroller {
    @Resource
    courseservice courseservice;
    @RequestMapping("/getcoueselist")
    public List<Map<String,Integer>> getlist(){
        List<Course> res=courseservice.getcourse();
        List<Map<String,Integer>> courseList=new ArrayList<>();
        for (int i = 0; i < courseservice.getcourse().size(); i++) {
            Map<String,Integer> map=new HashMap<>();
            map.put(res.get(i).getCourseName(),res.get(i).getCourseSection());
            courseList.add(map);
        }
        System.out.println("说藏话了");
        return courseList;
    }
}
