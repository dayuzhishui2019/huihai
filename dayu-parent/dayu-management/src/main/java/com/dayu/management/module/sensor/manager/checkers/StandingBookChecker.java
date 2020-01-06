package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.google.common.base.Strings;
import com.leus.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StandingBookChecker implements Checker, Register<SensorChecker> {


    @Autowired
    private SensorChecker checker;

    @Autowired
    private StandingBookIni ini;

    @Override
    public boolean test(List<String> items) {
        if (Objects.isNullOrEmpty(items) || items.size() != 13) {
            return false;
        }
        //检验国标 要么不填,要么填对
        if (!(Objects.isNullOrEmpty(items.get(StandingBook.GID)) || items.get(StandingBook.GID).matches("\\d{48}"))) {
            return false;
        }
        //校验设备名称
        if (Strings.nullToEmpty(items.get(StandingBook.NAME)).trim().isEmpty()) {
            return false;
        }
        //校验设备类型
        if (Objects.isNullOrEmpty(items.get(StandingBook.TYPE)) || !items.get(StandingBook.TYPE).matches(ini.getTypeRegular())) {
            return false;
        }
        //行政区划
        if (!(Objects.isNullOrEmpty(items.get(StandingBook.AREA_NUMBER)) || items.get(StandingBook.AREA_NUMBER).matches("\\d+"))) {
            return false;
        }
        //管辖单位
        if (!(Objects.isNullOrEmpty(items.get(StandingBook.DOMINION_CODE)) || items.get(StandingBook.DOMINION_CODE).matches("\\d+"))) {
            return false;
        }
        //所属平台
        if (Objects.isNullOrEmpty(items.get(StandingBook.PLATFORM))) {
            return false;
        }
        return checker.getCheckers(items.get(StandingBook.TYPE)).test(items);
    }

    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register("StandingBook", this);
    }
}
