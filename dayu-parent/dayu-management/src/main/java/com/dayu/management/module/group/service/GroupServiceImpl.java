package com.dayu.management.module.group.service;

import com.dayu.management.constant.BusinessError;
import com.dayu.management.core.Query;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.group.helper.GroupHelper;
import com.dayu.management.module.group.helper.GroupSQLHelper;
import com.dayu.management.module.group.mapper.GroupMapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;
import com.dayu.management.module.group.model.TaskResourceIds;
import com.dayu.management.module.group.model.TreeNode;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.FileWriteMode;
import com.leus.common.base.Objects;
import com.leus.common.util.StreamUtil;
import com.leus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper mapper;

    @Autowired
    private DatabaseHelper dbHelper;

    @Autowired
    private GroupHelper groupHelper;


    @Value("${dayu.group.resource.path:/data/resource}")
    private String resourceData;


    @Override
    public boolean addGroup(List<Group> groups) {
        List<Group> inserts = Lists.newArrayList();
        List<Group> updates = Lists.newArrayList();
        groups.forEach(group -> {
            if (Strings.isNullOrEmpty(group.getId())) {
                inserts.add(group);
            } else {
                updates.add(group);
            }
        });

        inserts.forEach(group -> {
            if (Strings.isNullOrEmpty(group.getId())) {
                group.setId(UUIDUtil.randomUUIDw());
            } else {
                Assert.isTrue(group.getId().matches("[0-9a-z]{32}"), BusinessError.STATE_CHECK_ERROR.message("groupId不符合校验规则(32位长度)"));
            }
            if (Strings.isNullOrEmpty(group.getParentId())) {
                group.setParentId(String.format("%032d", 0));
            }
        });
        if (!inserts.isEmpty()) {
            mapper.insert(inserts);
        }
        if (!updates.isEmpty()) {
            mapper.update(updates);
        }
        return true;
    }

    @Override
    public List<Group> queryGroup(GroupQuery query) {
        return mapper.select(Query.create().with(query));
    }

    @Override
    public boolean addNodesToGroup(String groupId, List<String> nodeIds) throws IOException, SQLException {
        //先删除
        mapper.deleteRelation(groupId, nodeIds);
        //新增
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            for (String nodeId : nodeIds) {
                String line = String.format("%s,%s,%s\n", UUIDUtil.randomUUIDw(), groupId, nodeId);
                try {
                    output.write(line.getBytes());
                } catch (IOException e) {
                    log.error("构建Group relation CSV出错", e);
                }
            }
            output.flush();
            input = new ByteArrayInputStream(output.toByteArray());
            dbHelper.copyIn("group_sensor_relation", input);
        } finally {
            StreamUtil.close(input, output);
        }
        return true;
    }

    @Override
    public List<TreeNode> queryTree(String groupId) {
        List<TreeNode> nodes = mapper.selectByParentId(Lists.newArrayList(groupId));
        if (nodes == null) {
            return nodes;
        }
        return nodes.stream()
                .sorted((treeNode, t1) -> treeNode.getName().compareToIgnoreCase(t1.getName()))
                .sorted(Comparator.comparingInt(TreeNode::getNodeType))
                .collect(Collectors.toList());
    }

    @Override
    public String createResource(TaskResourceIds ids) throws IOException, SQLException {

        Assert.isTrue(!(Objects.isNullOrEmpty(ids.getNodeIds()) && Objects.isNullOrEmpty(ids.getParentIds())), RunningError.STATE_CHECK_ERROR.message("组织与设备集合不能全为空"));
        List<String> allBranchIds = Lists.newLinkedList();
        String resourceId = UUIDUtil.randomUUIDw();
        Path dataPath = Paths.get(resourceData);
        if (!Files.exists(dataPath)) {
            Files.createDirectories(dataPath);
        }
        Path resource = dataPath.resolve(resourceId);
        Writer writer = com.google.common.io.Files.asCharSink(resource.toFile(), Charset.forName("utf-8"), FileWriteMode.APPEND).openBufferedStream();
        if (!Objects.isNullOrEmpty(ids.getParentIds())) {
            ids.getParentIds().forEach(id -> allBranchIds.addAll(groupHelper.getAllBranch(id)));
            String querySQL1 = GroupSQLHelper.selectOnlyLeafByParentId(allBranchIds);
            dbHelper.copyOut(querySQL1, writer);
        }
        if (!Objects.isNullOrEmpty(ids.getNodeIds())) {
            String querySQL2 = GroupSQLHelper.selectByNodeId(ids.getNodeIds());
            dbHelper.copyOut(querySQL2, writer);
        }
        writer.flush();
        return resourceId;
    }

    @Override
    public InputStream getResource(String resourceId) throws IOException {
        Path data = Paths.get(resourceData).resolve(resourceId);
        Assert.isTrue(Files.exists(data), BusinessError.RESOURCE_NOT_FOUND);
        return Files.newInputStream(data);
    }


}
