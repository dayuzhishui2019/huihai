package com.dayu.management.module.task.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{



    @Override
    public List<Box> queryBoxes(Query query) {
        return null;
    }

    @Override
    public List<Task> queryTasks(Query query) {
        return null;
    }

    @Override
    public String hasNewVersion(String taskId) {
        return null;
    }

    @Override
    public void updateCallback(String taskId) {

    }

    @Override
    public Task create(Task task) {
        return null;
    }

    @Override
    public Task update(Task task) {
        return null;
    }
}
