package com.kzh.busi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kzh.sys.service.generate.auto.*;
import com.kzh.sys.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@QClass(name = "课程")
@Entity
@Table(name = "b_course")
public class Course extends BaseEntity {
    @QField(name = "名称", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String name; //名称

    @ManyToOne
    @JoinColumn(name = "grade_id")
    @JsonManagedReference
    private Grade grade; //所属年级

    @ManyToMany(cascade = {CascadeType.REMOVE}, mappedBy = "courses")
    @JsonBackReference
    private Set<Clazz> clazzes;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_course_teacher", joinColumns = {@JoinColumn(name = "courses_id")}, inverseJoinColumns = {@JoinColumn(name = "teachers_id")})
    @JsonManagedReference
    private Set<Teacher> teachers;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_course_student")
    @JsonManagedReference
    private Set<Student> students;

    @Transient
    private List<String> clazzIds = new ArrayList<>();

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public List<String> getClazzIds() {
        return clazzIds;
    }

    public void setClazzIds(List<String> clazzIds) {
        this.clazzIds = clazzIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Clazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(Set<Clazz> clazzes) {
        this.clazzes = clazzes;
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
