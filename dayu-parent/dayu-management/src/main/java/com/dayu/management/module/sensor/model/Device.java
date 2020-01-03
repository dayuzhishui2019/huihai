package com.dayu.management.module.sensor.model;

import lombok.Data;

@Data
public class Device<T extends ToCsvLine> {

    private T derive;

    private Sensor sensor;

}
