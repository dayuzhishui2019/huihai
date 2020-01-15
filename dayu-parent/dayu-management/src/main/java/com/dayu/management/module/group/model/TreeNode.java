package com.dayu.management.module.group.model;

import lombok.Data;

@Data
public class TreeNode {

    private String id;

    private String parentId;

    private String name;

    private int type;

    private int func;

    private int nodeType;

}
