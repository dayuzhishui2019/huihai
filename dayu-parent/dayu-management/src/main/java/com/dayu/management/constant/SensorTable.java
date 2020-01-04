package com.dayu.management.constant;

import com.google.common.base.Strings;
import lombok.Getter;

public enum SensorTable {

    MAIN_TABLE("", "sensor"),
    CAMERA("摄像机", "camera"),
    MONITOR("卡口", "monitor");

    SensorTable(String label, String tableName) {
        this.label = label;
        this.tableName = tableName;
    }

    @Getter
    private String label;

    @Getter
    private String tableName;


    public static SensorTable labelOf(String label) {
        if (Strings.isNullOrEmpty(label)) {
            return null;
        }
        label = label.trim();

        SensorTable[] tables = values();
        for (SensorTable table : tables) {
            if (table.getLabel().equals(label)) {
                return table;
            }
        }
        return null;
    }
}
