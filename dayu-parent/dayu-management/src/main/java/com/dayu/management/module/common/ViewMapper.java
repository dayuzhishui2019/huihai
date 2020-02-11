package com.dayu.management.module.common;

import org.apache.ibatis.annotations.Param;

public interface ViewMapper {

    //刷新物化视图
    void refresh(@Param("view") String view);

}
