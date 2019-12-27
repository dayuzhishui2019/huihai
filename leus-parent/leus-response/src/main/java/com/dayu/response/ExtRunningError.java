package com.dayu.response;

public enum ExtRunningError implements RunningError {

    STATE_CHECK_ERROR("5001", "运行时检查异常");

    private String code;

    private String message;

    ExtRunningError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
