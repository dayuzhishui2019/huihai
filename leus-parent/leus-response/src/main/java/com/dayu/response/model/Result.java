package com.dayu.response.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class Result<T> {

    private String code;

    private String message;

    private T data;

    private Object exception;

}
