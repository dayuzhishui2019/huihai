package com.dayu.management.api;

import com.dayu.management.core.Query;
import com.dayu.management.module.task.model.Box;
import com.dayu.management.module.task.model.BoxQuery;
import com.dayu.management.module.task.model.Task;
import com.dayu.management.module.task.service.BoxService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(value = "节点管理", tags = "节点管理")
@Controller
@RequestMapping("box")
@Slf4j
public class BoxResource {


    @Autowired
    private BoxService boxService;


    @ApiOperation("注册与心跳接口,返回当前盒子上的任务列表")
    @ResponseBody
    @PostMapping("heart")
    public List<Task> registerWithHeartBeat(@RequestBody Box box) {
        return boxService.register(box);
    }


    @ApiOperation("修改盒子名称等信息")
    @ResponseBody
    @PostMapping("update")
    public Box update(@RequestBody Box box) {
        return boxService.updateBox(box);
    }


    @ApiOperation("获取所有盒子列表")
    @ResponseBody
    @PostMapping
    public List<Box> query(@RequestBody BoxQuery boxQuery) {
        return boxService.queryBoxes(Query.create().with(boxQuery));
    }
}
