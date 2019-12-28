package com.dayu;

import com.dayu.management.DayuManagementApplication;
import com.dayu.management.module.user.helper.SessionManager;
import com.dayu.management.module.user.mapper.CertificateMapper;
import com.dayu.management.module.user.mapper.UserMapper;
import com.dayu.management.module.user.model.Certificate;
import com.dayu.management.module.user.model.LoginResponse;
import com.dayu.management.module.user.model.User;
import com.dayu.management.module.user.model.query.LoginQuery;
import com.dayu.management.module.user.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Map;

@SpringBootTest(classes = {DayuManagementApplication.class})// 指定启动类
@Slf4j
public class Tests {


    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CertificateMapper certificateMapper;

    @Autowired
    private SessionManager manager;


    @Test
    public void addUser() {
        User user = new User();
        Certificate certificate = new Certificate();

        user.setId(UUIDUtil.randomUUIDw());
        user.setName("大禹");
        user.setUsername("admin");
        user.setStatus(0);
        certificate.setId(UUIDUtil.randomUUIDw());
        certificate.setUserId(user.getId());
        certificate.setType(1);
        certificate.setValue("admin");
        user.setCertificates(Lists.newArrayList(certificate));

        Assert.isTrue(userMapper.insert(Lists.newArrayList(user)) == 1, "");
        Assert.isTrue(certificateMapper.insert(user.getCertificates()) == 1, "");

    }

    @Test
    public void login() {
        LoginQuery query = new LoginQuery();
        query.setUsername("admin");
        query.setCertificate("admin");
        query.setType(1);

        LoginResponse response = userService.login(query);

        Assert.isTrue(response != null, "");

    }

    @Test
    public void sss() {

        Map<String, Object> map = Maps.newHashMap();
        map.put("admin", true);

        manager.setAttribute("D9FD468FF15A41D989DD94B5D7D2801C", map);


    }

}
