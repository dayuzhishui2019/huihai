package com.dayu.management.module.user.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.module.user.model.Session;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionMapper extends Mapper<Session> {


    int saveOrUpdate(Session session);


}
