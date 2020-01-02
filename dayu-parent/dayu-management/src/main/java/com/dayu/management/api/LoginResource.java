package com.dayu.management.api;

import com.dayu.management.constant.IdentifierNamed;
import com.dayu.management.module.user.model.LoginResult;
import com.dayu.management.module.user.model.query.LoginQuery;
import com.dayu.management.module.user.service.UserService;
import com.dayu.management.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Api(value = "用户认证", tags = "用户认证")
@RestController
@RequestMapping
public class LoginResource {

    @Autowired
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("login")
    public LoginResult login(@RequestBody LoginQuery query) {
        LoginResult loginResult = userService.login(query);
        HttpServletResponse response = RequestUtils.getCurrentResponse();
        Cookie cookie = new Cookie(IdentifierNamed.SESSION_ID, loginResult.getSession().getId());
        response.addCookie(cookie);
        return loginResult;
    }


    @ApiOperation("获取当前登录信息")
    @GetMapping("current")
    public LoginResult getCurrentLogin(@CookieValue(IdentifierNamed.SESSION_ID) String dayuId) {
        return userService.current(dayuId);
    }

}
