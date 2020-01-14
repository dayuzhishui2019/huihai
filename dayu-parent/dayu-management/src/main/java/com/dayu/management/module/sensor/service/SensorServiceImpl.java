package com.dayu.management.module.sensor.service;

import com.dayu.management.constant.BusinessError;
import com.dayu.management.constant.SensorTable;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.core.Query;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.dayu.management.module.sensor.manager.SensorConverter;
import com.dayu.management.module.sensor.manager.checkers.Cause;
import com.dayu.management.module.sensor.mapper.SensorMapper;
import com.dayu.management.module.sensor.model.Device;
import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.derive.Camera;
import com.dayu.management.module.sensor.model.ext.Channel;
import com.dayu.response.Assert;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;
import com.leus.common.util.StreamUtil;
import com.leus.common.util.UUIDUtil;
import com.leus.common.util.Values;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
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

    @Autowired
    private SensorMapper mapper;


    private String newLine = System.getProperty("line.separator");

    private final String mainTable = "sensor";

    private final Splitter splitter = Splitter.on(",");

    @Override
    public Map<String, Integer> importFile(File file) throws IOException {
        CharSource source = Files.asCharSource(file, Charset.forName("utf8"));
        List<String> errorLineNumber = Lists.newLinkedList();
        long a = System.currentTimeMillis();
        SensorChecker.Checkers checkers = checker.getStandingBookChecker();
        int[] count = new int[]{0};
        source.lines().skip(1).forEach(line -> {
            count[0]++;
            Cause cause = checkers.test(splitter.splitToList(line));
            if (!cause.isSuccess()) {
                errorLineNumber.add(String.format("%d:%s", count[0], cause.getCause()));
            }
        });

        long b = System.currentTimeMillis();

        log.info("检测耗时 {}ms", b - a);

        Assert.isTrue(errorLineNumber.isEmpty(), BusinessError.STATE_CHECK_ERROR.message("存在错误行数", Values.set("errorLine", errorLineNumber)));

        Map<String, File> tables = buildFiles(UUIDUtil.randomUUID(), source.lines().parallel());

        long c = System.currentTimeMillis();
        log.info("文件耗时 {}ms", c - b);
        tables.entrySet().parallelStream().forEach(entry -> {
            FileInputStream input = null;
            BufferedInputStream buffer = null;
            try {
                input = new FileInputStream(entry.getValue());
                buffer = new BufferedInputStream(input);
                helper.copyIn(entry.getKey(), buffer);
                entry.getValue().deleteOnExit();
            } catch (Exception e) {
                log.error("COPY IN FILE", e);
            } finally {
                StreamUtil.close(buffer, input);
            }
        });

        long d = System.currentTimeMillis();
        log.info("入库耗时 {}ms", d - c);
        Map<String, Integer> result = Maps.newHashMap();
        result.put("SUCCESS", count[0] - 1);
        return result;
    }

    @Override
    public void exportFile(String query, OutputStream outputStream) throws IOException, SQLException {
        helper.copyOut(query, outputStream);
    }

    @Override
    public List<Sensor> querySensor(Query query) {
        return mapper.select(query);
    }

    @Override
    public int count(Query query) {
        return mapper.count(query);
    }


    private Map<String, File> buildFiles(String randomFlag, Stream<String> lines) throws IOException {
        Map<String, Writer> writers = Maps.newConcurrentMap();
        Map<String, File> files = Maps.newConcurrentMap();
        File sensorFile = new File(String.format("%s-%s.csv", randomFlag, mainTable));
        files.put(mainTable, sensorFile);
        Writer mainSinK = Files.asCharSink(sensorFile, Charset.forName("utf8"), FileWriteMode.APPEND).openBufferedStream();
        writers.put("main", mainSinK);
        lines.skip(1).forEach(line -> {
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
                    if (device.getDerive() instanceof Camera) {
                        File channelFile = new File(String.format("%s-%s.csv", randomFlag, "channel"));
                        Writer channelWriter = Files.asCharSink(channelFile, Charset.forName("utf8"), FileWriteMode.APPEND).openBufferedStream();
                        files.put("channel", channelFile);
                        writers.put("channel", channelWriter);

                    }
                }
                if (device.getDerive() != null) {
                    writers.get(tableName).write(device.getDerive().toCsvLine() + newLine);
                    if (device.getDerive() instanceof Camera) {
                        List<Channel> channels = ((Camera) device.getDerive()).getChannels();
                        Writer writer = writers.get("channel");
                        for (Channel channel : channels) {
                            writer.write(channel.toCsvLine() + newLine);
                        }
                    }
                }
                mainSinK.write(device.getSensor().toCsvLine() + newLine);
            } catch (Exception e) {
                log.error("Convert item", e);
            }
        });

        flushWriters(writers);
        //关闭所有的Writer
        closeWriters(writers);
        return files;
    }


    private void flushWriters(Map<String, Writer> writers) {
        //关闭所有的Writer
        writers.values().parallelStream().forEach(w -> {
            try {
                w.flush();
            } catch (IOException e) {
                log.error("Convert flush", e);
            } finally {
                // StreamUtil.close(w);
            }
        });
    }

    private void closeWriters(Map<String, Writer> writers) {
        //关闭所有的Writer
        writers.values().parallelStream().forEach(w -> StreamUtil.close(w));
    }
}
