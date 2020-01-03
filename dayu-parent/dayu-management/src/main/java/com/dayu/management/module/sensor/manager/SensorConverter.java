package com.dayu.management.module.sensor.manager;

import com.dayu.management.module.sensor.manager.convert.Convert;
import com.dayu.management.module.sensor.model.Device;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SensorConverter {

    private Map<String, Convert> convertMap = Maps.newHashMap();


    public void register(String named, Convert convert) {
        convertMap.put(named, convert);
    }

    public Converter getConverter(String named) {
        return new Converter(convertMap.get(named));
    }

    public class Converter {

        private Convert convert;

        private Converter(Convert convert) {
            this.convert = convert;
        }

        public Device convert(String line) {
            return convert.convert(line);
        }
    }
}
