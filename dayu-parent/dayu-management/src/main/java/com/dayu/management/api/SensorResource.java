package com.dayu.management.api;

import com.dayu.management.constant.BusinessError;
import com.dayu.management.core.Query;
import com.dayu.management.module.sensor.helper.SQLCheckers;
import com.dayu.management.module.sensor.model.Sensor;
import com.dayu.management.module.sensor.model.SensorQuery;
import com.dayu.management.module.sensor.service.SensorService;
import com.dayu.management.utils.ResponseUtils;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.dayu.response.RunningError;
import com.dayu.response.model.Result;
import com.google.common.io.Files;
import com.leus.common.base.Objects;
import com.leus.common.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import java.util.List;
import java.util.Map;

@Api(value = "设备管理", tags = "设备管理")
@Controller
@RequestMapping("sensor")
@Slf4j
public class SensorResource {

    @Autowired
    private SensorService service;

    @ResponseBody
    @PostMapping
    public WebAsyncTask<Result<Map<String, Integer>>> importFile(MultipartFile file, HttpServletRequest request) {
        WebAsyncTask<Result<Map<String, Integer>>> task = new WebAsyncTask<>(1000 * 60 * 5, () -> {
            String sessionId = request.getSession().getId();
            log.info("SessionId:{}", sessionId);
            File file1 = new File(UUIDUtil.randomUUIDw() + "-" + file.getOriginalFilename());
            Files.asByteSink(file1).writeFrom(file.getInputStream());
            Map<String, Integer> v = service.importFile(file1);
            file1.deleteOnExit();
            return Result.<Map<String, Integer>>builder().code("200").message("导入成功").data(v).build();
        });
        task.onTimeout(() -> Result.<Map<String, Integer>>builder().code(RunningError.FAIL.getCode()).message("导入超时").build());
        task.onError(() -> Result.<Map<String, Integer>>builder().code(RunningError.FAIL.getCode()).message("导入出错").build());
        return task;
    }


    @ResponseBody
    @PostMapping("list")
    public List<Sensor> querySensor(@RequestBody SensorQuery query) {
        Assert.isTrue(query != null, BusinessError.STATE_CHECK_ERROR.message("Query不能为空"));
        return service.querySensor(Query.create().with(query));
    }

    @ResponseBody
    @PostMapping("count")
    public int count(@RequestBody SensorQuery query) {
        Assert.isTrue(query != null, BusinessError.STATE_CHECK_ERROR.message("Query不能为空"));
        return service.count(Query.create().with(query));
    }


    @ApiOperation("根据SQL检索相应数据 返回CSV格式")
    @GetMapping(value = "{filename}")
    public void export(@PathVariable("filename") @DefaultValue("export.csv") String fileName, @RequestParam String query, HttpServletResponse response) throws IOException, SQLException {
        Assert.isTrue(!Objects.isNullOrEmpty(query), ExtRunningError.STATE_CHECK_ERROR);
        Assert.isTrue(SQLCheckers.select(query), ExtRunningError.STATE_CHECK_ERROR);
        service.exportFile(query, ResponseUtils.decorate(response, fileName).getOutputStream());
    }

}
