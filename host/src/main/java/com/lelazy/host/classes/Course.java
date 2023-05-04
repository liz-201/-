package com.lelazy.host.classes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "course")
public class Course {
    @Id
    @Column(name = "course_id", nullable = false)
    private Integer id;

    @Column(name = "course_cacategory")
    private String courseCacategory;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_section")
    private Integer courseSection;

    public Integer getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(Integer courseSection) {
        this.courseSection = courseSection;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCacategory() {
        return courseCacategory;
    }

    public void setCourseCacategory(String courseCacategory) {
        this.courseCacategory = courseCacategory;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}