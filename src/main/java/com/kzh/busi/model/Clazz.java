package com.kzh.busi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kzh.sys.service.generate.auto.*;
import com.kzh.sys.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

//班级有多少学生？班级-学生
//班级有多少老师？班级-老师
//班级有多少课程？班级-课程
@QClass(name = "班级")
@Entity
@Table(name = "b_clazz")
public class Clazz extends BaseEntity {
    @QField(name = "名称", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like, nullable = false)
    private String name; //名称

    @ManyToOne
    @JoinColumn(name = "grade_id")
    @JsonManagedReference
    private Grade grade; //班级所属年级

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_clazz_course")
    @JsonManagedReference
    private Set<Course> courses;//班级与课程是直接关联的，与有没有老师无关

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_clazz_teacher", joinColumns = {@JoinColumn(name = "clazzes_id")}, inverseJoinColumns = {@JoinColumn(name = "teachers_id")})
    @JsonManagedReference
    private Set<Teacher> teachers;//班级与老师是直接关联的，与有没有课程也无关

    @OneToMany(mappedBy = "clazz")
    @JsonBackReference
    private Set<Student> students;//学生

    @Transient
    private List<String> courseIds = new ArrayList<>();

    @Transient
    private List<String> teacherIds = new ArrayList<>();

    //---get/set---------------

    public List<String> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

    public List<String> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<String> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
