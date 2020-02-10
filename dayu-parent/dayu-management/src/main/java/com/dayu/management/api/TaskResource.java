package com.dayu.management.api;

import com.dayu.management.core.Query;
import com.dayu.management.module.group.model.TaskResourceIds;
import com.dayu.management.module.group.service.GroupService;
import com.dayu.management.module.task.model.Task;
import com.dayu.management.module.task.model.TaskForm;
import com.dayu.management.module.task.model.TaskQuery;
import com.dayu.management.module.task.service.TaskService;
import com.dayu.management.utils.ResponseUtils;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.leus.common.base.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@Api(value = "任务管理", tags = "任务管理")
@Controller
@RequestMapping("task")
@Slf4j
public class TaskResource {

    @Autowired
    private TaskService taskService;

    @Autowired
    private GroupService groupService;

    @ApiOperation("创建任务(需要先建资源)")
    @ResponseBody
    @PostMapping
    public Task create(@RequestBody Task task) {
        Assert.isTrue(!Strings.isNullOrEmpty(task.getBoxId()), RunningError.STATE_CHECK_ERROR.message("资源节点ID不能为空"));
        Assert.isTrue(!Strings.isNullOrEmpty(task.getResourceId()), RunningError.STATE_CHECK_ERROR.message("任务资源不能为空"));
        return taskService.create(task);
    }

    @ApiOperation("创建任务(一键创建任务)")
    @ResponseBody
    @PostMapping("assemble")
    public Task create(@RequestBody TaskForm form) throws IOException, SQLException {
        Assert.isTrue(form.getTask() != null, RunningError.STATE_CHECK_ERROR.message("任务不能为空"));
        Assert.isTrue(!Strings.isNullOrEmpty(form.getTask().getBoxId()), RunningError.STATE_CHECK_ERROR.message("资源节点ID不能为空"));
        Assert.isTrue(form.getResourceIds() != null, RunningError.STATE_CHECK_ERROR.message("任务资源不能为空"));
        String resourceId = groupService.createResource(form.getResourceIds());
        form.getTask().setResourceId(resourceId);
        return taskService.create(form.getTask());
    }

    @ApiOperation("根据任务ID更新任务信息")
    @ResponseBody
    @PutMapping
    public boolean update(@RequestBody Task task) {
        Assert.isTrue(!Objects.isNullOrEmpty(task.getId()), RunningError.STATE_CHECK_ERROR.message("任务ID不能为空"));
        taskService.update(task);
        return true;
    }


    @ApiOperation("根据任务ID获取任务")
    @ResponseBody
    @GetMapping("{taskId:[0-9a-z]{32}}")
    public Task get(@PathVariable("taskId") String taskId) {
        return taskService.getTask(taskId);
    }

    @ApiOperation("删除任务")
    @ResponseBody
    @PostMapping("delete")
    public boolean delete(@RequestBody List<String> ids) {
        return taskService.delete(ids);
    }


    @ApiOperation("检索任务列表")
    @ResponseBody
    @PostMapping("list")
    public List<Task> list(@RequestBody TaskQuery query) {
        return taskService.queryTasks(Query.create().with(query));
    }

    @ApiOperation("统计任务总数")
    @ResponseBody
    @PostMapping("count")
    public int count(@RequestBody TaskQuery query) {
        return taskService.countTasks(Query.create().with(query));
    }

    @ApiOperation("根据资源ID获取资源")
    @ResponseBody
    @GetMapping("resource/{resourceId}")
    public void getResource(@PathVariable String resourceId, HttpServletResponse response) throws IOException {
        InputStream dataStream = groupService.getResource(resourceId);
        ByteStreams.copy(dataStream, ResponseUtils.decorate(response, resourceId).getOutputStream());
    }

    @ApiOperation("任务资源创建,返回资源Id")
    @PostMapping("/resource")
    public String createResource(@RequestBody TaskResourceIds ids) throws IOException, SQLException {
        Assert.isTrue(ids != null, RunningError.STATE_CHECK_ERROR.message("参数不能为空"));
        Assert.isTrue(!(Objects.isNullOrEmpty(ids.getParentIds()) && Objects.isNullOrEmpty(ids.getNodeIds())), RunningError.STATE_CHECK_ERROR.message("ParentIds与NodeIds不能同时为空"));
        return groupService.createResource(ids);
    }

}
