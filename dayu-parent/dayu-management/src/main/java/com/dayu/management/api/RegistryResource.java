package com.dayu.management.api;


import com.dayu.management.module.registry.RegistryService;
import com.dayu.response.Assert;
import com.dayu.response.RunningError;
import com.dayu.response.model.Result;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(value = "仓库管理", tags = "仓库管理")
@RestController
@RequestMapping("registry")
@Slf4j
public class RegistryResource {

    @Autowired
    private RegistryService service;

    @ApiOperation("上传镜像")
    @PostMapping("images/{repository}/{tag}")
    public WebAsyncTask<Result<Boolean>> pushImage(@PathVariable("repository") String repository,
                                                   @PathVariable("tag") String tag,
                                                   MultipartFile image) throws IOException {
        Assert.isTrue(!Strings.isNullOrEmpty(repository), RunningError.STATE_CHECK_ERROR.message("repository 不能为空"));
        Assert.isTrue(!Strings.isNullOrEmpty(tag), RunningError.STATE_CHECK_ERROR.message("tag 不能为空"));
        Assert.isTrue(image.getOriginalFilename().endsWith("tar"), RunningError.STATE_CHECK_ERROR.message("请上传镜像包"));

        WebAsyncTask<Result<Boolean>> task = new WebAsyncTask<>(1000 * 60 * 5, () -> {
            boolean v = service.pushImage(repository, tag, image.getInputStream());
            return Result.<Boolean>builder().code(RunningError.SUCCESS.getCode()).message(RunningError.SUCCESS.getMessage()).data(v).build();

        });
        task.onTimeout(() -> Result.<Boolean>builder().code(RunningError.FAIL.getCode()).message("导入超时").build());
        task.onError(() -> Result.<Boolean>builder().code(RunningError.FAIL.getCode()).message("导入出错").build());
        return task;
    }

    @ApiOperation("获取仓库镜像列表")
    @GetMapping("images")
    public List<String> getImages() {
        return service.getImages();
    }

    @ApiOperation("获取镜像tags")
    @GetMapping("images/{repository}/tags/list")
    public List<String> getTags(@PathVariable("repository") String repository) {
        return service.getTags(repository);
    }


    @ApiOperation("删除镜像")
    @DeleteMapping("images/{repository}/{tag}")
    public boolean deleteImage(@PathVariable("repository") String repository, @PathVariable("tag") String tag) {
        return service.deleteImage(repository, tag);
    }


}
