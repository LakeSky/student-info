package com.kzh.busi.enums;

/**
 * Created by gang on 2018/4/11.
 */
//如果没有创建，在项目启动的时候会去创建这些角色
public enum BaseRole {
    ROLE_ROOT("系统管理员"), ROLE_PLATFORM("平台管理员"), COMMON("普通用户");

    private String name;

    BaseRole(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
