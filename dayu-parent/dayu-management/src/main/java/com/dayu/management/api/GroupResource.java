package com.dayu.management.api;

import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.TreeNode;
import com.dayu.management.module.group.service.GroupService;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.leus.common.base.Objects;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Api(value = "分组管理", tags = "分组管理")
@RestController
@RequestMapping("group")
@Slf4j
public class GroupResource {

    @Autowired
    private GroupService service;


    @ApiOperation("向Group下增加叶子节点")
    @PostMapping("{groupId:\\d{32}}")
    public boolean addNodesToGroup(@PathVariable("groupId") String groupId,
                                   @RequestBody List<String> nodeIds) throws IOException, SQLException {
        Assert.isTrue(!Objects.isNullOrEmpty(groupId), RunningError.STATE_CHECK_ERROR.message("groupId不能为空"));
        Assert.isTrue(!Objects.isNullOrEmpty(nodeIds), RunningError.STATE_CHECK_ERROR.message("nodeIds不能为空"));
        return service.addNodesToGroup(groupId, nodeIds);
    }


    @ApiOperation("获取组织下节点 返回JSON格式")
    @GetMapping("{groupId:\\d{32}}")
    public List<TreeNode> getByParentId(@PathVariable("groupId") @DefaultValue("-1") String groupId) {
        Assert.isTrue(!Objects.isNullOrEmpty(groupId), RunningError.STATE_CHECK_ERROR.message("groupId不能为空"));
        return service.queryTree(groupId);
    }

    @ApiOperation("增加Group分组 或 向Group节点下增加枝干节点")
    @PostMapping
    public boolean addGroup(@RequestBody List<Group> groups) {
        Assert.isTrue(!Objects.isNullOrEmpty(groups), RunningError.STATE_CHECK_ERROR.message("groups不能为空"));
        return service.addGroup(groups);
    }

}
