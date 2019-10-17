package com.kzh.busi.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gang on 2017/4/6.
 */
public enum DataLevel {
    ALL("全部", 10),
    COMPANY("公司", 20),
    ME("自己", 100);

    private String name;

    private Integer level;

    DataLevel(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public static List<DataLevel> getLteDataLevels(DataLevel topDataLevel) {
        List<DataLevel> dataLevels = new ArrayList<>();
        for (DataLevel dataLevel : DataLevel.values()) {
            if (dataLevel.getLevel() >= topDataLevel.getLevel()) {
                dataLevels.add(dataLevel);
            }
        }

        return dataLevels;
    }

    public static List<DataLevel> getLtDataLevels(DataLevel topDataLevel) {
        List<DataLevel> dataLevels = new ArrayList<>();
        for (DataLevel dataLevel : DataLevel.values()) {
            if (dataLevel.getLevel() > topDataLevel.getLevel()) {
                dataLevels.add(dataLevel);
            }
        }

        return dataLevels;
    }
}
