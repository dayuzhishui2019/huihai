package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.module.sensor.helper.LineItemHelper;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.google.common.base.Strings;
import com.leus.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CameraChecker implements Checker, Register<SensorChecker> {


    @Autowired
    private StandingBookIni ini;

    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register(SensorTable.CAMERA.getLabel(), this);
    }


    @Override
    public Cause test(List<String> lineItem) {
        if (!LineItemHelper.testFunc(ini, lineItem)) {
            return Cause.fail("摄像机功能不正确");
        }
        if (!(!Objects.isNullOrEmpty(lineItem.get(StandingBook.IP)) && ini.getIpRegular().matcher(lineItem.get(StandingBook.IP)).matches())) {
            return Cause.fail("IP地址不正确");
        }
        if (!(!Objects.isNullOrEmpty(lineItem.get(StandingBook.PORT)) && ini.getPortRegular().matcher(lineItem.get(StandingBook.PORT)).matches() && Integer.valueOf(lineItem.get(StandingBook.PORT)) <= 65535)) {
            return Cause.fail("端口号不正确");
        }
        if (Strings.isNullOrEmpty(lineItem.get(StandingBook.USERNAME))) {
            return Cause.fail("摄像机用户名为空");
        }
        if (Strings.isNullOrEmpty(lineItem.get(StandingBook.PASSWORD))) {
            return Cause.fail("摄像机密码为空");
        }
        if (!(!Strings.isNullOrEmpty(lineItem.get(StandingBook.AV_CHANNEL)) && ini.getChannelRegular().matcher(lineItem.get(StandingBook.AV_CHANNEL)).matches())) {
            return Cause.fail("通道号不正确");
        }
        return Cause.success();
    }

}
