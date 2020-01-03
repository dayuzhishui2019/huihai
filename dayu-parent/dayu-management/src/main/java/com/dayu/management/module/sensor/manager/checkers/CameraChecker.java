package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CameraChecker implements Checker, Register<SensorChecker> {

    @Override
    public boolean test(String line) {
        return false;
    }

    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register("camera", this);
    }
}
