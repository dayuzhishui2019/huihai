package com.dayu.management.module.group.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EqualsAndHashCode
public class GroupQuery {


    private int size;


    private int start;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 父节点
     */
    private String parent;

    /**
     * 组类型
     */
    private int groupType;

    /**
     * 节点类型
     */
    private int nodeType;
}
