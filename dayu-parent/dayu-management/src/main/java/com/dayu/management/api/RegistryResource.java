package com.dayu.management.api;


import com.dayu.management.module.registry.RegistryService;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.dayu.response.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Api(value = "仓库管理", tags = "仓库管理")
@RestController
@RequestMapping("registry")
@Slf4j
public class RegistryResource {

    @Autowired
    private RegistryService service;

    @ApiOperation("上传镜像")
    @PostMapping("images")
    public WebAsyncTask<Result<String>> pushImage(MultipartFile image) {
        Assert.notNull(image, RunningError.STATE_CHECK_ERROR.message("您没有传镜像"));
        Assert.isTrue(image.getOriginalFilename().endsWith("tar"), RunningError.STATE_CHECK_ERROR.message("请上传镜像包(*.tar)"));

        WebAsyncTask<Result<String>> task = new WebAsyncTask<>(1000 * 60 * 5, () -> {
            String imageName = service.pushImage(image.getInputStream());
            return Result.<String>builder().code(RunningError.SUCCESS.getCode()).message(RunningError.SUCCESS.getMessage()).data(imageName).build();

        });
        task.onTimeout(() -> Result.<String>builder().code(RunningError.FAIL.getCode()).message("导入超时").build());
        task.onError(() -> Result.<String>builder().code(RunningError.FAIL.getCode()).message("导入出错").build());
        return task;
    }

    @ApiOperation("获取仓库镜像列表")
    @GetMapping("images")
    public List<String> getImages() {
        return service.getImages();
    }

    @ApiOperation("获取镜像tags")
    @GetMapping("images/tags/list")
    public List<String> getTags(@RequestParam("repository") String repository) throws UnsupportedEncodingException {
        return service.getTags(URLDecoder.decode(repository, "utf8"));
    }


    @ApiOperation("删除镜像")
    @DeleteMapping("images/{repository}/{tag}")
    public boolean deleteImage(@PathVariable("repository") String repository, @PathVariable("tag") String tag) {
        return service.deleteImage(repository, tag);
    }


}
