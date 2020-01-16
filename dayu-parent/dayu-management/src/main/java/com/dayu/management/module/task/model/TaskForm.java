package com.dayu.management.module.task.model;

import com.dayu.management.module.group.model.TaskResourceIds;
import lombok.Data;

@Data
public class TaskForm {

    private Task task;


    private TaskResourceIds resourceIds;


}
