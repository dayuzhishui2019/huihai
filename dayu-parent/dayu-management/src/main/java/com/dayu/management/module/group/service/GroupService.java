package com.dayu.management.module.group.service;

import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;

import java.util.List;

public interface GroupService {


    List<Group> queryGroup(GroupQuery query);

}
