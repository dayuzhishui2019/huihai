package com.dayu.management.module.auth.service;

import java.io.File;
import java.util.Map;

public interface AuthService {


    File getUnAuthInfo();

    Map<String, Object> updateAuth();

}
