package com.dayu.management.module.user.service;


import com.dayu.management.module.user.model.LoginResponse;
import com.dayu.management.module.user.model.query.LoginQuery;

public interface UserService {

    LoginResponse login(LoginQuery query);


}
