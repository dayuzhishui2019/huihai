package com.dayu.management.module.sensor.model;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class DiverseSensorView {


    //直接新增的Line
    private Map<String, String> insertLines = Maps.newHashMap();

    //需要更新的Line
    private Map<String, String> updateLines = Maps.newHashMap();


}
