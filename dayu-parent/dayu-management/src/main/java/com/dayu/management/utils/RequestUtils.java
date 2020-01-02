package com.dayu.management.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
public class RequestUtils {


    private RequestUtils() {
    }

    public static HttpServletResponse getCurrentResponse() {
        return getCurrentRequestAttributes().getResponse();
    }

    public static HttpServletRequest getCurrentRequest() {
        return getCurrentRequestAttributes().getRequest();
    }

    public static String getCookieValue(String name) {
        Cookie[] cookies = getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if (name.equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    public static Cookie getCookie(String name) {
        Cookie[] cookies = getCurrentRequest().getCookies();
        for (Cookie cookie : cookies) {
            String cookieName = cookie.getName();
            if (name.equals(cookieName)) {
                return cookie;
            }
        }
        return null;
    }


    private static ServletRequestAttributes getCurrentRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    }


}
