package com.dayu.management.module.sensor.manager.convert;

import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.ToCsvLine;

public interface Convert<T extends ToCsvLine> {

    Device<T> convert(String line);
}
