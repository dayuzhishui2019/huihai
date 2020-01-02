package com.dayu.management.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApplicationContextUtils {

    private ApplicationContextUtils() {
    }

    private static ApplicationContext getContext() {
        return WebApplicationContextUtils.getWebApplicationContext(RequestUtils.getCurrentRequest().getServletContext());
    }


    public static <T> T getInstance(Class<T> tClass) {
        return getContext().getBean(tClass);
    }
}
