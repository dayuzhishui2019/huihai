package com.dayu.management.module.task.model;

import lombok.Data;

@Data
public class Task {

    //任务ID
    private String id;

    //任务名称
    private String name;

    //镜像名称
    private String repository;

    //当前版本
    private String currentTag;

    //更新时间
    private long updateTime;

    //任务接入的资源列表
    private String resourceId;

    //上一个版本
    private String previousTag;

    //任务创建时间
    private Long createTime;

    //接入类型
    private Integer accessType;

    //任务状态
    private Integer status;

    //待更新版本
    private String newTag;

    //所属硬件盒子ID <盒子如果未授权,任务不能下发>
    private String boxId;

}
