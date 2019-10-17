package com.kzh.busi.model;

import com.kzh.busi.enums.ExamType;
import com.kzh.sys.service.generate.auto.QClass;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@QClass(name = "考试")
public class Exam {

    private String name; //考试名称

    private Date time; //考试时间

    private ExamType type; //考试类型:默认为 年级统考，平时考试

    private String remark; //备注

    @ManyToOne
    @JoinColumn(name = "grade_id")
    private Grade grade; //考试年级

    @ManyToOne
    @JoinColumn(name = "clazz_id")
    private Clazz clazz; //考试的班级: 平时考试涉及到某个班级，统考则为所有班级

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; //考试科目：单科情况
}
