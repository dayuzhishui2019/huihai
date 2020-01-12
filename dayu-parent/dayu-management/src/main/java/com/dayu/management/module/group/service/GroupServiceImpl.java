package com.dayu.management.module.group.service;

import com.dayu.management.core.Query;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.group.helper.GroupHelper;
import com.dayu.management.module.group.helper.GroupSQLHelper;
import com.dayu.management.module.group.mapper.GroupMapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;
import com.dayu.management.module.group.model.TaskResourceIds;
import com.dayu.management.module.group.model.TreeNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.io.FileWriteMode;
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
import java.util.List;

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
        groups.forEach(group -> {
            if (Strings.isNullOrEmpty(group.getId())) {
                group.setId(UUIDUtil.randomUUIDw());
            }
        });

        return mapper.insert(groups) != 0;
    }

    @Override
    public List<Group> queryGroup(GroupQuery query) {
        return mapper.select(Query.create().with(query));
    }

    @Override
    public boolean addNodesToGroup(String groupId, List<String> nodeIds) throws IOException, SQLException {
        InputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            for (String nodeId : nodeIds) {
                String line = String.format("%s,%s,%s", UUIDUtil.randomUUIDw(), groupId, nodeId);
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
        return mapper.selectOnlyLeafByParentId(Lists.newArrayList(groupId));
    }

    @Override
    public String createResource(TaskResourceIds ids) throws IOException, SQLException {
        List<String> allBranchIds = Lists.newLinkedList();
        ids.getParentIds().forEach(id -> allBranchIds.addAll(groupHelper.getAllBranch(id)));
        String querySQL1 = GroupSQLHelper.selectOnlyLeafByParentId(allBranchIds);
        String querySQL2 = GroupSQLHelper.selectByNodeId(ids.getNodeIds());
        String resourceId = UUIDUtil.randomUUIDw();
        Path dataPath = Paths.get(resourceData);
        if (Files.exists(dataPath)) {
            Files.createDirectory(dataPath);
        }
        Path resource = dataPath.resolve(resourceId);
        Writer writer = com.google.common.io.Files.asCharSink(resource.toFile(), Charset.forName("utf-8"), FileWriteMode.APPEND).openBufferedStream();
        dbHelper.copyOut(querySQL1, writer);
        dbHelper.copyOut(querySQL2, writer);
        return resourceId;
    }


}
