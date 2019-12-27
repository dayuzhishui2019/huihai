package com.dayu.management.module.auth.service;

import java.util.Map;

public interface AuthService {


    Map<String, Object> getUnAuthInfo();

    Map<String, Object> updateAuth();

}
