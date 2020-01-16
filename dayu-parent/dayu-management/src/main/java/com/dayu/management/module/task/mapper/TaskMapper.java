package com.dayu.management.module.task.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Task;

public interface TaskMapper extends Mapper<Task> {


    int count(Query query);

}
