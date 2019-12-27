package com.dayu.response.exception;

import com.dayu.response.RunningError;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private RunningError error;

    public BusinessException(RunningError error) {
        super(error.getMessage());
        this.error = error;
    }

}
