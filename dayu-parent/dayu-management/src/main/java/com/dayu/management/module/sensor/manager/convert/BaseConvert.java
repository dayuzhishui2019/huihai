package com.dayu.management.module.sensor.manager.convert;

import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.ToCsvLine;
import com.google.common.base.Splitter;
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
        Sensor sensor = new Sensor();
        sensor.setId(UUIDUtil.randomUUIDw());
        sensor.setGid(items.get(StandingBook.GID));
        sensor.setName(items.get(StandingBook.NAME));
        sensor.setAddress(items.get(StandingBook.ADDRESS));
        sensor.setAreaNumber(items.get(StandingBook.AREA_NUMBER));
        sensor.setDominionCode(items.get(StandingBook.DOMINION_CODE));
        SensorTable table = SensorTable.labelOf(items.get(StandingBook.TYPE));
        sensor.setType(table.getSenorType());
        sensor.setSubtype(getSubTypes(table, items.get(StandingBook.SUB_TYPE)));
        sensor.setPlatform(items.get(StandingBook.PLATFORM));
        Device<T> device = new Device<>();
        device.setSensor(sensor);
        return device;
    }

    private int getSubTypes(SensorTable table, String lineItem) {
        List<String> items = splitter.splitToList(lineItem);
        Map<String, Integer> subTypes = table.getSubTypes();
        int subType = 0;
        for (String item : items) {
            subType |= subTypes.get(item);
        }
        return subType;
    }
}
