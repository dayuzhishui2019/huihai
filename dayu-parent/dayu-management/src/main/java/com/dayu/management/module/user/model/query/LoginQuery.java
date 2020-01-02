package com.dayu.management.module.user.model.query;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoginQuery {
    
    private String username;

    private String certificate;

    private int type;

}
