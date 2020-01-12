package com.dayu.management.module.task.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.Task;

import java.util.List;

public interface TaskService {

    //检索盒子
    List<Box> queryBoxes(Query query);

    //检索任务
    List<Task> queryTasks(Query query);

    //检索是否有新版本
    String hasNewVersion(String taskId);

    //更新成功后回调
    void updateCallback(String taskId);

    //创建一个任务
    Task create(Task task);

    //更新任务信息
    Task update(Task task);
}
