package com.dayu.response;

import com.dayu.response.exception.BusinessException;

public class Assert {

    private Assert() {
    }


    public static void isTrue(boolean exp, RunningError error) {
        if (!exp) {
            throw new BusinessException(error);
        }
    }


    public static <T> T notNull(T object, RunningError error) {
        if (object == null) {
            throw new BusinessException(error);
        }
        return object;
    }
}
