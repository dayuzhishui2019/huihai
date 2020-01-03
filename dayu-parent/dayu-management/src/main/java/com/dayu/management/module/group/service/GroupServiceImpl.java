package com.dayu.management.module.group.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.group.mapper.GroupMapper;
import com.dayu.management.module.group.model.Group;
import com.dayu.management.module.group.model.GroupQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupMapper mapper;

    @Override
    public List<Group> queryGroup(GroupQuery query) {
        return mapper.select(Query.create().with(query));
    }


}
