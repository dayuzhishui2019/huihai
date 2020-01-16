package com.dayu.management.module.task.service;

import com.dayu.management.constant.BusinessError;
import com.dayu.management.core.Query;
import com.dayu.management.module.task.mapper.TaskMapper;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.Task;
import com.dayu.response.Assert;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.leus.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper mapper;

    @Override
    public List<Box> queryBoxes(Query query) {
        return null;
    }

    @Override
    public List<Task> queryTasks(Query query) {
        return mapper.select(query);
    }

    @Override
    public int countTasks(Query query) {
        return mapper.count(query);
    }

    @Override
    public Task getTask(String taskId) {
        return mapper.get(taskId);
    }


    @Override
    public void updateCallback(String taskId) {
        Task task = new Task();
        task.setId(taskId);
        task.setNewTag("NONE");
        update(task);
    }

    @Override
    public Task create(Task task) {
        task.setId(UUIDUtil.randomUUIDw());
        task.setCreateTime(System.currentTimeMillis());
        task.setUpdateTime(System.currentTimeMillis());
        task.setStatus(0);
        int i = mapper.insert(Lists.newArrayList(task));
        Assert.isTrue(i != 0, BusinessError.STATE_CHECK_ERROR.message("任务创建失败"));
        return task;
    }

    @Override
    public Task update(Task task) {
        Assert.isTrue(task != null && !Strings.isNullOrEmpty(task.getId()), BusinessError.STATE_CHECK_ERROR.message("任务ID为空"));
        task.setUpdateTime(System.currentTimeMillis());
        Assert.isTrue(mapper.update(Lists.newArrayList(task)) != 0, BusinessError.STATE_CHECK_ERROR.message("更新任务失败"));
        return task;
    }

    @Override
    public boolean delete(List<String> ids) {
        return mapper.delete(ids) != 0;
    }
}
