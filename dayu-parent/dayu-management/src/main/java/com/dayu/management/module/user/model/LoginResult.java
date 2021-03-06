package com.dayu.management.module.user.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class LoginResult {

    private User user;

    private Session session;

}
