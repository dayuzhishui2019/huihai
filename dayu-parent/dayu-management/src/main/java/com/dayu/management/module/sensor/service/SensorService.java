package com.dayu.management.module.sensor.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface SensorService {


    Map<String, Integer> importFile(File file) throws IOException;

}
