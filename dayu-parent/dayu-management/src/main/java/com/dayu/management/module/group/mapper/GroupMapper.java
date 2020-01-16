package com.dayu.management.module.group.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.TreeNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GroupMapper extends Mapper<Group> {


    //查询父节点下的子节点(含树枝与树叶)
    List<TreeNode> selectByParentId(List<String> parentIds);


    List<TreeNode> selectByNodeId(List<String> nodeIds);


    List<TreeNode> selectOnlyLeafByParentId(List<String> parentIds);


    int deleteRelation(@Param("groupId") String groupId, @Param("sensorIds") List<String> sensorIds);


}
