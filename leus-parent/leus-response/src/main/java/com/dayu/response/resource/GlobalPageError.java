package com.dayu.response.resource;

import com.dayu.response.model.Result;
import com.dayu.response.annotation.IgnoreDecorate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
public class GlobalPageError {

    private final static String ERROR_PATH = "/error";

    @IgnoreDecorate
    @GetMapping(path = ERROR_PATH)
    public Result<?> error(HttpServletRequest request, HttpServletResponse response) {
        return Result.builder()
                .code(Objects.toString(response.getStatus()))
                .message(String.format("访问%s失败", request.getRequestURL()))
                .build();
    }


}
