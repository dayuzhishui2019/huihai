package com.dayu.management.api;

import com.dayu.management.module.sensor.helper.SQLCheckers;
import com.dayu.management.module.sensor.service.SensorService;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.dayu.response.RunningError;
import com.dayu.response.model.Result;
import com.google.common.io.Files;
import com.leus.common.base.Objects;
import com.leus.common.util.UUIDUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;

@Api(value = "设备管理", tags = "设备管理")
@Controller
@RequestMapping("sensor")
@Slf4j
public class SensorResource {

    @Autowired
    private SensorService sensor;

    @ResponseBody
    @PostMapping
    public WebAsyncTask<Result<Object>> importFile(MultipartFile file, HttpServletRequest request) {
        WebAsyncTask<Result<Object>> task = new WebAsyncTask<>(1000 * 60 * 5, () -> {
            String sessionId = request.getSession().getId();
            log.info("SessionId:{}", sessionId);
            File file1 = new File(UUIDUtil.randomUUIDw() + "-" + file.getOriginalFilename());
            Files.asByteSink(file1).writeFrom(file.getInputStream());
            Map<String, Integer> v = sensor.importFile(file1);
            file1.deleteOnExit();
            return Result.builder().code("200").message("导入成功").data(v).build();
        });
        task.onTimeout(() -> Result.builder().code(RunningError.FAIL.getCode()).message("导入超时").build());
        task.onError(() -> Result.builder().code(RunningError.FAIL.getCode()).message("导入出错").build());
        return task;
    }


    @GetMapping(value = "{filename}")
    public void export(@PathVariable("filename") @DefaultValue("export.csv") String fileName, @RequestParam String query, HttpServletResponse response) throws IOException, SQLException {
        Assert.isTrue(!Objects.isNullOrEmpty(query), ExtRunningError.STATE_CHECK_ERROR);
        String sql = new String(Base64.getDecoder().decode(query), "utf-8");
        Assert.isTrue(!SQLCheckers.select(sql), ExtRunningError.STATE_CHECK_ERROR);
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        sensor.exportFile(sql, response.getOutputStream());
    }

}
