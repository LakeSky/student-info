package com.kzh.busi.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum Sex {
    MALE("男"),
    FEMALE("女")
    ;

    private String name;

    Sex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
