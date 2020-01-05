package com.dayu.management.api;

import com.dayu.management.module.sensor.service.SensorService;
import com.google.common.io.Files;
import com.leus.common.util.UUIDUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

@Api(value = "设备管理", tags = "设备管理")
@RestController
@RequestMapping("sensor")
@Slf4j
public class SensorResource {

    @Autowired
    private SensorService sensor;

    @PostMapping(value = "import")
    public WebAsyncTask<Boolean> importFile(MultipartFile file, HttpServletRequest request) {
        WebAsyncTask<Boolean> task = new WebAsyncTask<>(1000 * 60 * 5, () -> {
            String sessionId = request.getSession().getId();
            log.info("SessionId:{}", sessionId);
            File file1 = new File(UUIDUtil.randomUUIDw() + "-" + file.getOriginalFilename());
            Files.asByteSink(file1).writeFrom(file.getInputStream());
            sensor.importFile(file1);
            file1.deleteOnExit();
            return true;
        });
        return task;


    }

}
