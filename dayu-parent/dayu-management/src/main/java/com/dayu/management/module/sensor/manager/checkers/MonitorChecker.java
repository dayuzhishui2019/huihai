package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.SensorTable;
import com.dayu.management.module.sensor.helper.LineItemHelper;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MonitorChecker implements Checker, Register<SensorChecker> {

    @Autowired
    private StandingBookIni ini;

    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register(SensorTable.MONITOR.getLabel(), this);
    }

    @Override
    public Cause test(List<String> lineItem) {
        return LineItemHelper.testFunc(ini, lineItem) ? Cause.success() : Cause.fail("卡口功能不正确");
    }
}
