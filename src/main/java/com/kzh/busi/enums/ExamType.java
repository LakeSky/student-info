package com.kzh.busi.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum ExamType {
    NORMAL("平时"),
    GRADE("年级"),
    ;

    private String name;

    ExamType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
