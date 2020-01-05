package com.dayu.response;

import lombok.Getter;

public enum ExtRunningError implements RunningError {

    STATE_CHECK_ERROR("5001", "运行时检查异常");

    @Getter
    private String code;

    @Getter
    private String message;

    ExtRunningError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
