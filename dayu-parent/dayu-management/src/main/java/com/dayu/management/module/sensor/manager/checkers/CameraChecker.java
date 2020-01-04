package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.helper.LineItemHelper;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CameraChecker implements Checker, Register<SensorChecker> {


    @Autowired
    private StandingBookIni ini;


    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register(SensorTable.CAMERA.getLabel(), this);
    }


    @Override
    public boolean test(List<String> lineItem) {
        if (!LineItemHelper.testSubTypes(ini, lineItem)) {
            return false;
        }
        if (!(lineItem.get(StandingBook.IP) != null && lineItem.get(StandingBook.IP).matches(ini.getIpRegular()))) {
            return false;
        }
        if (!(lineItem.get(StandingBook.PORT) != null && lineItem.get(StandingBook.IP).matches("\\d{1,5}") && Integer.valueOf(lineItem.get(StandingBook.IP)) <= 65535)) {
            return false;
        }
        if (Strings.isNullOrEmpty(lineItem.get(StandingBook.USERNAME))) {
            return false;
        }
        if (Strings.isNullOrEmpty(lineItem.get(StandingBook.PASSWORD))) {
            return false;
        }
        if (!(!Strings.isNullOrEmpty(lineItem.get(StandingBook.AV_CHANNEL)) && lineItem.get(StandingBook.AV_CHANNEL).matches("([a-zA-Z0-9=]+;*){1,}"))) {
            return false;
        }
        return true;
    }

}
