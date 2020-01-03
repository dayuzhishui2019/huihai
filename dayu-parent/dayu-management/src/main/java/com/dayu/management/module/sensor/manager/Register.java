package com.dayu.management.module.sensor.manager;

import org.springframework.beans.factory.annotation.Autowired;

public interface Register<T> {

    void register(@Autowired T checker);

}
