package com.dayu.management.constant;

import com.google.common.base.Strings;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum SensorTable {

    MAIN_TABLE("sensor", "sensor", 0, new HashMap<String, Integer>() {
        {

        }
    }),
    CAMERA("摄像机", "camera", 1, new HashMap<String, Integer>() {
        {
            put("人脸", 1);
            put("人体", 2);
            put("机动车", 4);
            put("非机动车", 8);
        }
    }),
    MONITOR("卡口", "monitor", 2, new HashMap<String, Integer>() {
        {
            put("人脸", 1);
            put("人体", 2);
            put("机动车", 4);
            put("非机动车", 8);
        }
    });

    SensorTable(String label, String tableName, int senorType, Map<String, Integer> subTypes) {
        this.label = label;
        this.tableName = tableName;
        this.senorType = senorType;
        this.subTypes = subTypes;

    }

    @Getter
    private String label;

    @Getter
    private String tableName;

    @Getter
    private int senorType;

    @Getter
    private Map<String, Integer> subTypes;


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
