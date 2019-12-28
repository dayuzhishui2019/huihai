package com.dayu.management.module.user.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.user.helper.SessionManager;
import com.dayu.management.module.user.mapper.CertificateMapper;
import com.dayu.management.module.user.mapper.UserMapper;
import com.dayu.management.module.user.model.Certificate;
import com.dayu.management.module.user.model.LoginResponse;
import com.dayu.management.module.user.model.Session;
import com.dayu.management.module.user.model.User;
import com.dayu.management.module.user.model.query.LoginQuery;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.leus.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private SessionManager sessionManager;

    @Value("${management.session.duration:180000}")
    private int duration;

    @Override
    public LoginResponse login(LoginQuery query) {
        List<User> users = userMapper.select(Query.create(1, 0).with(query));
        Preconditions.checkState(users != null && !users.isEmpty() && users.size() == 1, "用户名或密码错误");
        List<Certificate> certificates = certificateMapper.select(Query.create(1, 0)
                .set("userId", users.get(0).getId())
                .set("type", query.getType()));
        Preconditions.checkState(certificates != null && !certificates.isEmpty() && certificates.size() == 1, "用户名或密码错误");

        Preconditions.checkState(query.getCertificate().equals(certificates.get(0).getValue()), "用户名或密码错误");
        //成功了 用户信息 需要写入Session
        //返回 用户登录成功的消息

        Map<String, Object> sessionValue = Maps.newHashMap();
        //  加入角色信息
        //  阿西吧 真尼玛蛋疼
        //  加入权限信息
        long now = System.currentTimeMillis();
        Session session = Session.builder()
                .id(UUIDUtil.randomUUIDw().toUpperCase())
                .duration(duration)
                .loginTime(now)
                .expireTime(now + duration)
                .userId(users.get(0).getId())
                .value(sessionValue)
                .build();
        sessionManager.save(session);
        //清理认证信息
        users.get(0).setCertificates(null);
        return LoginResponse.builder().session(session).user(users.get(0)).build();
    }
}
