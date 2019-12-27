package com.dayu.management.module.user.model;

import lombok.Data;

import java.util.List;

@Data
public class User {

    private String id;

    private String name;

    private String icon;

    private String username;

    private int status;

    private List<Certificate> certificates;

}
