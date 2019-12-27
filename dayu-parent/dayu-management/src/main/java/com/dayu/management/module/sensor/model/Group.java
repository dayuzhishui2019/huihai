package com.dayu.management.module.sensor.model;

import lombok.Data;

@Data
public class Group {

    private String id;

    private String roleId;

    private int groupType;

    private String nodeId;

    private int nodeType;

    private String parent;

}
