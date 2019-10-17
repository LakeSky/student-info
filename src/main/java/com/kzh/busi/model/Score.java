package com.kzh.busi.model;

import com.kzh.sys.service.generate.auto.QClass;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 考试成绩类
 *
 * @author bojiangzhou
 */
@QClass(name = "成绩")
public class Score {
    private Integer score; //考试成绩

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course; //考试科目

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam; //考试

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student; //考试学生
}
