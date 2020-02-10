package com.dayu.management.module.auth.service;

import com.dayu.management.utils.SystemUtils;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

public class AuthServiceImpl implements AuthService {


    @Override
    public File getUnAuthInfo() {

        Map<String, Object> value = Maps.newHashMap();

        String sn = SystemUtils.getProcessID();

        return null;
    }

    @Override
    public Map<String, Object> updateAuth() {
        return null;
    }


    public static void main(String[] args) {


    }
}
