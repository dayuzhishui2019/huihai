package com.dayu.response.resource;

import com.dayu.response.annotation.IgnoreDecorate;
import com.dayu.response.model.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
public class GlobalPageError {

    private final static String ERROR_PATH = "/error";

    @IgnoreDecorate
    @GetMapping(path = ERROR_PATH)
    public Result<?> error(HttpServletResponse response) {
        return Result.builder()
                .code(Objects.toString(response.getStatus()))
                .message("请求失败")
                .build();
    }


}
