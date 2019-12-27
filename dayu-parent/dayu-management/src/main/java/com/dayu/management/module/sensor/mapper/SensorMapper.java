package com.dayu.management.module.sensor.mapper;

import com.dayu.management.core.Mapper;
import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.query.SensorQuery;

import java.util.List;

public interface SensorMapper extends Mapper<Sensor> {


    List<Sensor> select(SensorQuery query);


}
