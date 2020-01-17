package com.dayu.management.module.task.model;

import lombok.Data;

//硬件
@Data
public class Box {

    //盒子ID
    private String id;

    //名称
    private String name;

    //硬件型号
    private String model;


    //第一次上线时间
    private long firstUpTime;

    //最近一次通讯时间
    private long lastConnectTime;

    //盒子序列号
    private String serialNumber;

    //盒子状态
    private int status;

    //随机码
    private String random;

}
