package com.dayu.management.utils;

import javax.servlet.http.HttpServletResponse;

public class ResponseUtils {
    private ResponseUtils() {
    }

    public static HttpServletResponse decorate(HttpServletResponse response, String fileName) {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        return response;
    }
}
