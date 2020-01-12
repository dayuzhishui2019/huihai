package com.dayu.management.constant;

import com.dayu.response.RunningError;

public enum BusinessError implements RunningError {


    NOT_LOGIN("4011", "用户未登录或会话已过期"),

    LOGIN_ERROR("4012", "用户名或密码错误"),

    RESOURCE_NOT_FOUND("4041", "任务资源不存在");

    private String code;

    private String message;

    BusinessError(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
