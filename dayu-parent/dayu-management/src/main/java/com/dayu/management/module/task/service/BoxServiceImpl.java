package com.dayu.management.module.task.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.mapper.BoxMapper;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.Task;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.leus.common.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxMapper mapper;

    @Autowired
    private TaskService taskService;

    @Value("${dayu.management.max-box-number:1000}")
    private int maxBoxNumber;

    @Override
    public List<Task> register(Box box) {
        Assert.isTrue(!Strings.isNullOrEmpty(box.getSerialNumber()), RunningError.STATE_CHECK_ERROR.message("节点序列号不能为空"));
        Assert.isTrue(!Strings.isNullOrEmpty(box.getModel()), RunningError.STATE_CHECK_ERROR.message("节点型号不能为空"));
        box.setLastConnectTime(System.currentTimeMillis());
        if (Strings.isNullOrEmpty(box.getId())) {
            //型号与序列号作为ID的唯一编号
            box.setId(MD5Util.sign(String.format("%s|%s|%s", box.getModel(), box.getSerialNumber(), box.getRandom()).getBytes(Charset.forName("utf-8"))));
            box.setFirstUpTime(System.currentTimeMillis());
            box.setStatus(0);
            mapper.insert(Lists.newArrayList(box));
            return Lists.newArrayList();
        }
        return taskService.queryTasks(Query.create(maxBoxNumber, 0).set("boxId", box.getId()));
    }

    @Override
    public Box updateBox(Box box) {
        Assert.isTrue(!Strings.isNullOrEmpty(box.getId()), RunningError.STATE_CHECK_ERROR.message("盒子ID不能为空"));
        return mapper.update(Lists.newArrayList(box)) != 0 ? box : null;
    }

    @Override
    public List<Box> queryBoxes(Query query) {
        return mapper.select(query);
    }

}
