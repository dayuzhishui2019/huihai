package com.dayu.management.module.sensor.manager.checkers;

import lombok.Data;

@Data
public class Cause {

    private boolean success;

    private String cause;

    private Cause(boolean success, String cause) {
        this.success = success;
        this.cause = cause;
    }


    public static Cause success() {
        return new Cause(true, null);
    }

    public static Cause fail(String message) {
        return new Cause(false, message);
    }

}
