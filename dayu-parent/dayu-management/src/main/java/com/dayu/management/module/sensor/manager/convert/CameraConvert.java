package com.dayu.management.module.sensor.manager.convert;

import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.derive.Camera;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraConvert implements Convert<Camera>, Register<SensorConverter> {
    @Override
    public Device<Camera> convert(String line) {
        ///
        return null;
    }

    @Autowired
    @Override
    public void register(SensorConverter checker) {

    }
}