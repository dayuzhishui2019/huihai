package com.dayu.management.module.user.model.query;

import lombok.Data;

@Data
public class LoginQuery {

    private String username;

    private String certificate;

    private int type;

}
