package com.dayu.management.module.sensor.manager.convert;

import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.derive.Camera;
import com.dayu.management.module.sensor.model.ext.Channel;
import com.leus.common.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CameraConvert implements Convert<Camera>, Register<SensorConverter> {

    @Autowired
    private BaseConvert convert;

    @Override
    public Device<Camera> convert(List<String> items) {
        Device<Camera> device = convert.convert(items);

        Camera camera = new Camera();
        camera.setId(UUIDUtil.randomUUIDw());
        camera.setIp(items.get(StandingBook.IP));
        camera.setPort(items.get(StandingBook.PORT));
        camera.setUsername(items.get(StandingBook.USERNAME));
        camera.setPassword(items.get(StandingBook.PASSWORD));

        device.getSensor().setDeriveId(camera.getId());

        List<Channel> channels = getChannels(device.getSensor().getId(), convert.getSplitter().splitToList(items.get(StandingBook.AV_CHANNEL)));
        camera.setChannels(channels);
        device.setDerive(camera);
        return device;
    }


    private List<Channel> getChannels(String sensorId, List<String> items) {
        return items.stream().collect(Collectors.mapping(value -> {
            Channel channel = new Channel();
            channel.setId(UUIDUtil.randomUUIDw());
            channel.setValue(value);
            channel.setSenorId(sensorId);
            return channel;
        }, Collectors.toList()));
    }


    @Autowired
    @Override
    public void register(SensorConverter checker) {
        checker.register(SensorTable.CAMERA.getLabel(), this);
    }


}
