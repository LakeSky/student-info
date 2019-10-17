package com.kzh.busi.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum LogType {
    login("登录"),
    ;

    private String name;

    LogType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
