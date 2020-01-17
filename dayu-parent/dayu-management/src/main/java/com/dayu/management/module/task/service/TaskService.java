package com.dayu.management.module.task.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Task;

import java.util.List;

public interface TaskService {


    //检索任务
    List<Task> queryTasks(Query query);

    //计算任务数目
    int countTasks(Query query);


    Task getTask(String taskId);

    //更新成功后回调
    void updateCallback(String taskId);

    //创建一个任务
    Task create(Task task);

    //更新任务信息
    Task update(Task task);

    boolean delete(List<String> ids);
}
