package com.dayu.management.module.user.model;

import lombok.Data;

@Data
public class Certificate {

    private String id;

    private String userId;

    private int type;

    private String value;

}
