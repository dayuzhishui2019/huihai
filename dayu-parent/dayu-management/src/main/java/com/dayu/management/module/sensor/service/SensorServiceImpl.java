package com.dayu.management.module.sensor.service;

import com.dayu.management.module.sensor.manager.SensorChecker;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.leus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Service
public class SensorServiceImpl implements SensorService {


    @Autowired
    private SensorChecker checker;

    @Autowired
    private SensorConverter converter;

    @Override
    public void importFile(String sensorType, File file) {
        Preconditions.checkState(file.getName().toLowerCase().endsWith("csv"), "请上传CSV类型模板");
        CharSource source = Files.asCharSource(file, Charset.forName("utf8"));
        List<Integer> errorLineNumber = Lists.newLinkedList();
        try {
            SensorChecker.Checkers checkers = checker.getCheckers(sensorType);
            int[] count = new int[]{1};
            source.lines().skip(0).forEach(line -> {
                if (!checkers.test(line)) {
                    errorLineNumber.add(count[0]);
                }
                count[0]++;
            });
        } catch (IOException e) {
            log.error("", e);
        }

        Assert.isTrue(errorLineNumber.isEmpty(), ExtRunningError.STATE_CHECK_ERROR);

        String deriveTmp = UUIDUtil.randomUUIDw() + "-" + sensorType + "-" + file.getName();
        String sensorTmp = UUIDUtil.randomUUIDw() + "-sensor-" + file.getName();

        try {
            Writer deriveSink = Files.asCharSink(new File(deriveTmp), Charset.forName("utf-8"), FileWriteMode.APPEND).openBufferedStream();
            Writer sensorSink = Files.asCharSink(new File(sensorTmp), Charset.forName("utf-8"), FileWriteMode.APPEND).openBufferedStream();

            SensorConverter.Converter convert = converter.getConverter(sensorType);

            source.lines().forEach(line -> {
                Device device = convert.convert(line);
                try {
                    deriveSink.append(device.getDerive().toCsvLine());
                    sensorSink.append(device.getSensor().toCsvLine());
                } catch (IOException e) {
                    log.error("", e);
                }
            });
            deriveSink.flush();
            sensorSink.flush();
        } catch (IOException e) {
            log.error("", e);
        }
    }
}
