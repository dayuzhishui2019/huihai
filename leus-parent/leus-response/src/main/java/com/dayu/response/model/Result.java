package com.dayu.response.model;

import lombok.Builder;

@Builder
public class Result<T> {

    private String code;

    private String message;

    private T data;

    private Object exception;

}
