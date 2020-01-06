package com.dayu.management.module.sensor.model;

import com.alibaba.fastjson.JSON;
import com.dayu.management.utils.CsvLineUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class Sensor implements ToCsvLine {

    /**
     * 传感器ID
     */
    private String id;

    /**
     * 传感器国标ID
     */
    private String gid;

    /**
     * 传感器名称
     */
    private String name;

    /**
     * 安装地点
     */
    private String address;

    /**
     * 行政区划代码
     */
    private String areaNumber;

    /**
     * 管辖单位代码
     */
    private String dominionCode;

    /**
     * 传感器类型
     */
    private int type;

    /**
     * 传感器子类型
     */
    private int subtype;


    /**
     * 扩展类型ID ,例如 指向CameraID
     */
    private String deriveId;

    /**
     * 平台类型
     */
    private String platform;

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + JSON.toJSONString(this);
    }

    @Override
    public String toCsvLine() {
        return CsvLineUtils.join(
                id, gid, name, address, areaNumber, dominionCode, type, subtype, deriveId, platform
        );
    }
}
