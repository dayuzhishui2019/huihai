package com.dayu.management.module.group.service;

import com.dayu.management.core.Query;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.group.mapper.GroupMapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;
import com.dayu.management.module.group.model.TreeNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.leus.common.util.StreamUtil;
import com.leus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper mapper;

    @Autowired
    private DatabaseHelper helper;

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
            helper.copyIn("group_sensor_relation", input);
        } finally {
            StreamUtil.close(input, output);
        }
        return true;
    }

    @Override
    public List<TreeNode> queryTree(String groupId) {
        return mapper.selectOnlyLeafByParentId(Lists.newArrayList(groupId));
    }

}
