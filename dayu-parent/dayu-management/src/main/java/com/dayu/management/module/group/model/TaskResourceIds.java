package com.dayu.management.module.group.model;

import lombok.Data;

import java.util.List;

@Data
public class TaskResourceIds {

    //组织机构IDs
    private List<String> parentIds;

    //设备IDs
    private List<String> nodeIds;


}
