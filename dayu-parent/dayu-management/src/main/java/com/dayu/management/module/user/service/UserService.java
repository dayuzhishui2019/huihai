package com.dayu.management.module.user.service;


import com.dayu.management.module.user.model.LoginResult;
import com.dayu.management.module.user.model.query.LoginQuery;

public interface UserService {

    LoginResult login(LoginQuery query);


    LoginResult current(String sessionId);


}
