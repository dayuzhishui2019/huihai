package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.google.common.base.Strings;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.leus.common.base.Objects;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.List;

@Component
public class StandingBookChecker implements Checker, Register<SensorChecker> {


    @Getter
    private final BloomFilter<String> GID_FILTER;


    @Autowired
    private SensorChecker checker;

    @Autowired
    private StandingBookIni ini;


    public StandingBookChecker() {
        GID_FILTER = BloomFilter.create((Funnel<String>) (value, sink) ->
                        sink.putString(value, Charset.forName("utf-8"))
                , 5000000);
    }


    @Override
    public boolean test(List<String> items) {
        if (Objects.isNullOrEmpty(items) || items.size() != 13) {
            return false;
        }
        //国标ID必填
        if (Objects.isNullOrEmpty(items.get(StandingBook.GID)) || !items.get(StandingBook.GID).matches("\\d{48}")) {
            return false;
        } else if (GID_FILTER.mightContain(items.get(StandingBook.GID))) {
            return false;
        }
        GID_FILTER.put(items.get(StandingBook.GID));

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
