package com.kzh.busi.enums;

/**
 * Created by gang on 2017/4/10.
 */
public enum BusiDirType {
    upload("upload"), tmp("tmp");

    private String name;

    BusiDirType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
