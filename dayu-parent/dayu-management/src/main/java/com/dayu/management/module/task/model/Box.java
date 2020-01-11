package com.dayu.management.module.task.model;

import lombok.Data;

//硬件
@Data
public class Box {

    //盒子ID
    private String id;

    //硬件型号
    private String model;

    //授权状态
    private boolean authorize;

    //是否在线
    private boolean online;

    //第一次上线时间
    private long firstUpTime;

    //最近一次通讯时间
    private long lastConnectTime;

    //盒子序列号
    private String serialNumber;

}
