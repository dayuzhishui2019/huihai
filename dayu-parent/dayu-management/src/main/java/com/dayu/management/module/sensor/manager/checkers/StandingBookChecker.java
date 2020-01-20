package com.dayu.management.module.sensor.manager.checkers;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.StandingBook;
import com.dayu.management.helper.DatabaseHelper;
import com.dayu.management.module.sensor.manager.Register;
import com.dayu.management.module.sensor.manager.SensorChecker;
import com.google.common.base.Strings;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.io.Files;
import com.leus.common.base.Objects;
import com.leus.common.util.UUIDUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;

@Slf4j
@Component
public class StandingBookChecker implements Checker, Register<SensorChecker> {


    @Getter
    private BloomFilter<String> GID_FILTER;

    @Autowired
    private DatabaseHelper db;


    @Autowired
    private SensorChecker checker;

    @Autowired
    private StandingBookIni ini;

    @Autowired
    public void init() {
        GID_FILTER = BloomFilter.create((Funnel<String>) (value, sink) ->
                        sink.putString(value, Charset.forName("utf-8"))
                , 5000000, 0.00000000000001);
        try {
            log.info("开始加载布隆过滤器");
            long a = System.currentTimeMillis();
            File f = new File(UUIDUtil.randomUUIDw());
            Writer writer = Files.asCharSink(f, Charset.forName("utf8")).openBufferedStream();
            db.copyOut("select gid from sensor", writer);
            writer.flush();
            Files.asCharSource(f, Charset.forName("utf8")).lines().forEach(line -> GID_FILTER.put(line));
            f.delete();
            log.info("加载布隆过滤器耗时:{}ms", System.currentTimeMillis() - a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Cause test(List<String> items) {
        if (Objects.isNullOrEmpty(items) || items.size() != 13) {
            return Cause.fail("空行或者非台账标准");
        }
        //国标ID必填
        if (Objects.isNullOrEmpty(items.get(StandingBook.GID)) || !items.get(StandingBook.GID).matches("\\d{24}")) {
            return Cause.fail("国标不符合标准");
        } else if (GID_FILTER.mightContain(items.get(StandingBook.GID))) {
            return Cause.fail("国标重复");
        }
        GID_FILTER.put(items.get(StandingBook.GID));

        //校验设备名称
        if (Strings.nullToEmpty(items.get(StandingBook.NAME)).trim().isEmpty()) {
            return Cause.fail("设备名称");
        }
        //校验设备类型
        if (Objects.isNullOrEmpty(items.get(StandingBook.TYPE)) || !items.get(StandingBook.TYPE).matches(ini.getTypeRegular())) {
            return Cause.fail("不支持的设备类型");
        }
        //行政区划
        if (!(Objects.isNullOrEmpty(items.get(StandingBook.AREA_NUMBER)) || items.get(StandingBook.AREA_NUMBER).matches("\\d+"))) {
            return Cause.fail("行政区划编码不正确");
        }
        //管辖单位
        if (!(Objects.isNullOrEmpty(items.get(StandingBook.DOMINION_CODE)) || items.get(StandingBook.DOMINION_CODE).matches("\\d+"))) {
            return Cause.fail("管辖单位编码不正确");
        }
        //所属平台
        if (Objects.isNullOrEmpty(items.get(StandingBook.PLATFORM))) {
            return Cause.fail("所属平台为空");
        }
        return checker.getCheckers(items.get(StandingBook.TYPE)).test(items);
    }

    @Autowired
    @Override
    public void register(SensorChecker checker) {
        checker.register("StandingBook", this);
    }
}
