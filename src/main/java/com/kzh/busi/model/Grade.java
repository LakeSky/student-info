package com.kzh.busi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kzh.sys.model.BaseEntity;
import com.kzh.sys.service.generate.auto.Action;
import com.kzh.sys.service.generate.auto.QClass;
import com.kzh.sys.service.generate.auto.QField;
import com.kzh.sys.service.generate.auto.QFieldQueryType;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

//年级有多少学生？年级-班级-学生
//年级有多少老师？年级-班级-老师
@QClass(name = "年级")
@Entity
@Table(name = "b_grade")
public class Grade extends BaseEntity {
    @QField(name = "名称", actions = {Action.edit, Action.show, Action.query}, queryType = QFieldQueryType.like, nullable = false)
    private String name; //名称

    @OneToMany(mappedBy = "grade")
    @JsonBackReference
    private Set<Clazz> clazzes; //班级

    @OneToMany(mappedBy = "grade")
    @JsonBackReference
    private Set<Course> courses; //课程

    //---get/set---------------

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
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
}
