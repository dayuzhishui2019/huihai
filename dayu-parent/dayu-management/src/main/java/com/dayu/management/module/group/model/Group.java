package com.dayu.management.module.group.model;

import lombok.Data;

@Data
public class Group {

    private String id;

    private String name;

    private int type;

    private String parentId;

}
