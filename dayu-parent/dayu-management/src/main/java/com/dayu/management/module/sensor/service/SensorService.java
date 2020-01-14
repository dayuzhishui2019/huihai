package com.dayu.management.module.sensor.service;

import com.dayu.management.core.Query;
import com.dayu.management.module.sensor.model.Sensor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SensorService {


    Map<String, Integer> importFile(File file) throws IOException;


    void exportFile(String query, OutputStream output) throws IOException, SQLException;


    List<Sensor> querySensor(Query query);

    int count(Query query);


}
