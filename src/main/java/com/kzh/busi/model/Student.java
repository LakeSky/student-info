package com.kzh.busi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kzh.busi.enums.Sex;
import com.kzh.sys.service.generate.auto.*;
import com.kzh.sys.model.BaseEntity;

import java.util.Set;
import javax.persistence.*;

//允许学生直接选老师
@QClass(name = "学生")
@Entity
@Table(name = "b_student")
public class Student extends BaseEntity {
    @QField(name = "学号", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String no; //学号

    @QField(name = "姓名", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String name; //姓名

    @QField(name = "性别", actions = {Action.edit, Action.show})
    private Sex sex; //性别

    @QField(name = "电话", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String phone; //电话

    @QField(name = "QQ", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like)
    private String qq; //QQ

    @ManyToOne
    @JoinColumn(name = "clazz_id")
    @JsonManagedReference
    private Clazz clazz; //班级所属班级

    @ManyToMany(cascade = {CascadeType.REMOVE}, mappedBy = "students")
    @JsonBackReference
    private Set<Course> courses;

    @ManyToMany(cascade = {CascadeType.REMOVE}, mappedBy = "students")
    @JsonBackReference
    private Set<Teacher> teachers;

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

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
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
}
