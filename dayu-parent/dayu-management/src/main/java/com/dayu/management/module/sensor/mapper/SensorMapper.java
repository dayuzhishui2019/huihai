package com.dayu.management.module.sensor.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.core.Query;
import com.dayu.management.module.sensor.model.Sensor;

import java.util.List;

public interface SensorMapper extends Mapper<Sensor> {


    List<Sensor> select(Query query);


}
