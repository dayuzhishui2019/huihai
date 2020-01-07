package com.dayu.management.module.group.service;

import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;
import com.dayu.management.module.group.model.TreeNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface GroupService {


    boolean addGroup(List<Group> group);

    List<Group> queryGroup(GroupQuery query);

    boolean addNodesToGroup(String groupId, List<String> nodeIds) throws IOException, SQLException;

    List<TreeNode> queryTree(String groupId);
}
