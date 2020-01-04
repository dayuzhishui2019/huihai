package com.dayu.management.module.sensor.service;

import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.leus.common.util.StreamUtil;
import com.leus.common.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class SensorServiceImpl implements SensorService {


    @Autowired
    private SensorChecker checker;

    @Autowired
    private SensorConverter converter;

    @Autowired
    private DatabaseHelper helper;

    private final String mainTable = "sensor";

    private final Splitter splitter = Splitter.on(",");

    @Override
    public void importFile(File file) {
        Preconditions.checkState(file.getName().toLowerCase().endsWith("csv"), "请上传CSV类型模板");
        CharSource source = Files.asCharSource(file, Charset.forName("utf8"));
        List<Integer> errorLineNumber = Lists.newLinkedList();
        try {
            SensorChecker.Checkers checkers = checker.getStandingBookChecker();
            int[] count = new int[]{1};
            source.lines().skip(0).forEach(line -> {
                if (!checkers.test(splitter.splitToList(line))) {
                    errorLineNumber.add(count[0]);
                }
                count[0]++;
            });
        } catch (IOException e) {
            log.error("checkFiles", e);
        }

        Assert.isTrue(errorLineNumber.isEmpty(), ExtRunningError.STATE_CHECK_ERROR);

        try {
            Map<String, File> tables = buildFiles(UUIDUtil.randomUUID(), source.lines());

            tables.forEach((table, data) -> {
                InputStream input = null;
                try {
                    input = new FileInputStream(data);
                    helper.copyIn(table, input);
                } catch (Exception e) {
                    log.error("COPY IN FILE", e);
                } finally {
                    StreamUtil.close(input);
                }
            });

        } catch (IOException e) {
            log.error("buildFiles", e);
        }

    }


    private Map<String, File> buildFiles(String randomFlag, Stream<String> lines) throws IOException {
        Map<String, Writer> writers = Maps.newHashMap();
        Map<String, File> files = Maps.newHashMap();
        File sensorFile = new File(String.format("%s-%s.csv", randomFlag, mainTable));
        files.put(mainTable, sensorFile);
        Writer mainSinK = Files.asCharSink(sensorFile, Charset.forName("utf8"), FileWriteMode.APPEND).openBufferedStream();
        writers.put("main", mainSinK);
        lines.forEach(line -> {
            try {
                List<String> items = splitter.splitToList(line);
                String tableLabel = items.get(StandingBook.TYPE);
                String tableName = SensorTable.labelOf(tableLabel).getTableName();
                Device<?> device = converter.getConverter(tableLabel).convert(items);
                if (device.getDerive() != null && !files.containsKey(tableName)) {
                    File file = new File(String.format("%s-%s.csv", randomFlag, tableName));
                    Writer deriveSink = Files.asCharSink(file, Charset.forName("utf8"), FileWriteMode.APPEND).openBufferedStream();
                    files.put(tableName, file);
                    writers.put(tableName, deriveSink);
                }
                if (device.getDerive() != null) {
                    writers.get(tableName).write(device.getDerive().toCsvLine());
                }
                mainSinK.write(device.getSensor().toCsvLine());
            } catch (Exception e) {
                log.error("Convert item", e);
            }
        });
        //关闭所有的Writer
        writers.values().stream().forEach(w -> {
            try {
                w.flush();
            } catch (IOException e) {
                log.error("Convert flush", e);
            } finally {
                StreamUtil.close(w);
            }
        });
        return files;
    }
}
