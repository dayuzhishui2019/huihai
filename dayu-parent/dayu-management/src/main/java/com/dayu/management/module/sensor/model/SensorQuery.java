package com.dayu.management.module.sensor.model;

import lombok.Data;

@Data
public class SensorQuery {


    private String name;

    private int type;

    private String areaNumber;

    private String dominionCode;

    private int start;

    private int size;

    private Boolean allocated;
}
