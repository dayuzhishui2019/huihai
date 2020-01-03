package com.dayu.management.module.sensor.model.derive;

import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.ext.Channel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Camera extends Sensor {

    /**
     * 传感器IP
     */
    private String ip;

    /**
     * 传感器端口
     */
    private String port;

    /**
     * 传感器用户名
     */
    private String username;

    /**
     * 传感器密码
     */
    private String password;

    /**
     * 通道ID
     */
    private List<Channel> channels;

    @Override
    public String toCsvLine() {
        return super.toCsvLine();
    }
}
