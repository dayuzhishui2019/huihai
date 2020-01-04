package com.dayu.management.module.sensor.model.ext;

import com.dayu.management.module.sensor.model.ToCsvLine;
import lombok.Data;

@Data
public class Channel implements ToCsvLine {

    /**
     * 通道ID
     */
    private String id;

    /**
     * 所属相机通道号
     */
    private String senorId;

    /**
     * 通道号
     */
    private String value;

    @Override
    public String toCsvLine() {
        return null;
    }
}
