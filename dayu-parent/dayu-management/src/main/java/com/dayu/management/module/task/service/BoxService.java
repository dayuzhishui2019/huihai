package com.dayu.management.module.task.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.Task;

import java.util.List;

/**
 * 盒子节点服务
 */
public interface BoxService {


    //盒子信息激活
    List<Task> register(Box box);


    //更新盒子信息
    Box updateBox(Box box);


    //检索盒子列表
    List<Box> queryBoxes(Query query);


}
