package com.dayu.management.module.user.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Session {

    /**
     * SessionID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 登录时间
     */
    private long loginTime;

    /**
     * 会话是有效时长
     */
    private int duration;

    /**
     * 过期时间
     */
    private long expireTime;

    /**
     * 会话里面的内容
     */
    private Map<String, Object> value;
}
