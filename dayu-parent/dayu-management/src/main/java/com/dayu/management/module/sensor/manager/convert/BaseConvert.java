package com.dayu.management.module.sensor.manager.convert;

import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.ToCsvLine;
import com.google.common.base.Splitter;
import com.leus.common.base.Objects;
import com.leus.common.util.UUIDUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BaseConvert<T extends ToCsvLine> implements Convert<T>, Register<SensorConverter> {

    @Getter
    private Splitter splitter = Splitter.on(";").trimResults().omitEmptyStrings();

    @Autowired
    @Override
    public void register(SensorConverter checker) {
        checker.register("sensor", this);
    }

    @Override
    public Device<T> convert(List<String> items) {
        String gid = items.get(StandingBook.GID);
        Sensor sensor = new Sensor();
        sensor.setId(UUIDUtil.randomUUIDw());
        sensor.setGid(items.get(StandingBook.GID));
        sensor.setName(items.get(StandingBook.NAME));
        sensor.setAddress(items.get(StandingBook.ADDRESS));

        String areaNumber = items.get(StandingBook.AREA_NUMBER);
        if (Objects.isNullOrEmpty(areaNumber)) {
            sensor.setAreaNumber(items.get(StandingBook.GID).substring(0, 6));
        } else {
            sensor.setAreaNumber(areaNumber);
        }

        String dominionCode = items.get(StandingBook.DOMINION_CODE);
        if (Objects.isNullOrEmpty(dominionCode)) {
            sensor.setDominionCode(items.get(StandingBook.GID).substring(0, 8));
        } else {
            sensor.setDominionCode(dominionCode);
        }

        SensorTable table = SensorTable.labelOf(items.get(StandingBook.TYPE));
        sensor.setType(table.getSenorType());
        sensor.setFunc(getFunc(table, items.get(StandingBook.FUNC)));
        sensor.setPlatform(items.get(StandingBook.PLATFORM));
        Device<T> device = new Device<>();
        device.setSensor(sensor);
        return device;
    }

    private int getFunc(SensorTable table, String lineItem) {
        List<String> items = splitter.splitToList(lineItem);
        Map<String, Integer> subTypes = table.getSubTypes();
        int subType = 0;
        for (String item : items) {
            subType |= subTypes.get(item);
        }
        return subType;
    }
}
