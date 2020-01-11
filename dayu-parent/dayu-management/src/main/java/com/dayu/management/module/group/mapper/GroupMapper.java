package com.dayu.management.module.group.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.TreeNode;

import java.util.List;


public interface GroupMapper extends Mapper<Group> {


    List<TreeNode> selectByParentId(List<String> parentIds);


    List<TreeNode> selectByNodeId(List<String> nodeIds);

}
