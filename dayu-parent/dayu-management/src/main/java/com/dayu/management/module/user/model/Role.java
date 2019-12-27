package com.dayu.management.module.user.model;

import lombok.Data;

@Data
public class Role {

    /**
     * 角色ID
     */
    private String id;

    /**
     * 功能权限
     */
    private String permission;

}
