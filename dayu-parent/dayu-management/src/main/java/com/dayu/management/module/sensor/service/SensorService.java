package com.dayu.management.module.sensor.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Map;

public interface SensorService {


    Map<String, Integer> importFile(File file) throws IOException;


    void exportFile(String query, OutputStream output) throws IOException, SQLException;





}
