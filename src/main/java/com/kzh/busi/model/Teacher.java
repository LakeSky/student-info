package com.kzh.busi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kzh.busi.enums.Sex;
import com.kzh.sys.service.generate.auto.*;
import com.kzh.sys.model.BaseEntity;
import com.kzh.sys.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@QClass(name = "教师")
@Entity
@Table(name = "b_teacher")
public class Teacher extends BaseEntity {
    @QField(name = "工号", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String no; //工号

    @QField(name = "姓名", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String name; //姓名

    @QField(name = "性别", actions = {Action.edit, Action.show})
    private Sex sex; //性别

    @QField(name = "电话", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String phone; //电话

    @QField(name = "QQ", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String qq; //QQ

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_course_teacher", joinColumns = {@JoinColumn(name = "teachers_id")}, inverseJoinColumns = {@JoinColumn(name = "courses_id")})
    @JsonBackReference
    private Set<Course> courses;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_clazz_teacher", joinColumns = {@JoinColumn(name = "teachers_id")}, inverseJoinColumns = {@JoinColumn(name = "clazzes_id")})
    @JsonBackReference
    private Set<Clazz> clazzes;

    @ManyToMany(cascade = {CascadeType.REMOVE})
    @JoinTable(name = "b_teacher_student")
    @JsonManagedReference
    private Set<Student> students;

    @Transient
    private List<String> courseIds = new ArrayList<>();

    @Transient
    private List<String> clazzIds = new ArrayList<>();

    public List<String> getCourseIds() {
        if (CollectionUtil.isNotEmpty(this.getCourses())) {
            for (Course course : this.getCourses()) {
                this.courseIds.add(course.getId());
            }
        }
        return courseIds;
    }

    public void setCourseIds(List<String> courseIds) {
        this.courseIds = courseIds;
    }

    public List<String> getClazzIds() {
        if (CollectionUtil.isNotEmpty(this.getClazzes())) {
            for (Clazz clazz : this.getClazzes()) {
                this.clazzIds.add(clazz.getId());
            }
        }
        return clazzIds;
    }

    public void setClazzIds(List<String> clazzIds) {
        this.clazzIds = clazzIds;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Set<Clazz> getClazzes() {
        return clazzes;
    }

    public void setClazzes(Set<Clazz> clazzes) {
        this.clazzes = clazzes;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
}
