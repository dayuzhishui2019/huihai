package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitorChecker implements Checker, Register<SensorChecker> {
    @Override
    public void register(SensorChecker checker) {

    }

    @Override
    public boolean test(List<String> lineItem) {
        return false;
    }
}
