package com.dayu.management.api;

import com.dayu.management.module.sensor.service.SensorService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@Api(value = "设备管理", tags = "设备管理")
@RestController
@RequestMapping("sensor")
public class SensorResource {

    @Autowired
    private SensorService sensor;

    @PostMapping("import")
    public boolean importFile(File file) throws IOException {
        sensor.importFile(file);
        return true;
    }

}
