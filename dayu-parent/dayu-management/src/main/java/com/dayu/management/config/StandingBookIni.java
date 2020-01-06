package com.dayu.management.config;

import com.beust.jcommander.internal.Lists;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


@Configuration
@ConfigurationProperties(prefix = "dayu.sensor")
public class StandingBookIni {

    @Getter
    @Value("${dayu.sensor.type-regular:摄像机|卡口|WIFI|RFID|GPS|电围|井盖|门禁|消防栓|烟杆}")
    private String typeRegular;

    @Getter
    private Pattern ipRegular = Pattern.compile("((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}");

    @Getter
    private Pattern channelRegular = Pattern.compile("([a-zA-Z0-9=]+;*){1,}");
    @Getter
    private Pattern portRegular = Pattern.compile("\\d{1,5}");

    private final Map<String, List<String>> subTypes = new HashMap<String, List<String>>() {

        {
            put("摄像机", Lists.newArrayList("人脸", "人体", "机动车", "非机动车"));
            put("卡口", Lists.newArrayList("人卡", "车卡"));
        }

    };


    public List<String> getSubTypes(String type) {
        return subTypes.get(type);
    }

}
