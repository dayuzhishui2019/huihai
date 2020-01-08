package com.dayu.management.module.registry.model;

import lombok.Data;

@Data
public class Version {

    private String id;

    private String taskId;

    //镜像内容
    private String repositoryName;

    //当前版本
    private String presentTag;

    //更新时间
    private long updateTime;

    //上一个版本
    private String previousTag;

}
