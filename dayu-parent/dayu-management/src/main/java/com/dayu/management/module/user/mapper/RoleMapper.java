package com.dayu.management.module.user.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.module.user.model.Certificate;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleMapper extends Mapper<Certificate> {


    int createNewGroup(String roleId);

}
