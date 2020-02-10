package com.dayu.management.module.sensor.model;

import lombok.Data;

import java.util.List;

@Data
public class CompareView {

    //老的
    private List<String> older;

    //新的
    private List<String> newer;
}
