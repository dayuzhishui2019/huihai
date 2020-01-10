package com.dayu.management.module.registry;

import com.dayu.management.module.registry.helper.RegistryClient;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.dayu.response.RunningError;
import com.dayu.response.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.LoadResponseItem;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.leus.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;


@Slf4j
@Service
public class RegistryServiceImpl implements RegistryService {


    @Autowired
    private DockerClient docker;

    @Autowired
    private RegistryClient registry;

    @Override
    public List<String> getImages() {
        return Assert.notNull(registry.getCataLog(), RunningError.STATE_CHECK_ERROR.message("获取镜像列表失败")).getRepositories();
    }

    @Override
    public List<String> getTags(String repository) {
        try {

            return Assert.notNull(registry.getTags(repository), RunningError.STATE_CHECK_ERROR.message("获取镜像Tag列表失败")).getTags();
        } catch (Exception e) {
            Assert.isTrue(false, ExtRunningError.STATE_CHECK_ERROR.message("获取镜像Tag列表失败"));
            return null;
        }
    }

    @Override
    public boolean deleteImage(String repository, String tag) {
        HttpServletResponse response = registry.getManifests(repository, tag);
        Assert.isTrue(HttpStatus.resolve(response.getStatus()).is2xxSuccessful(), ExtRunningError.STATE_CHECK_ERROR.message("删除失败"));
        String digestHash = response.getHeader("Docker-Content-Digest");
        Assert.isTrue(!Objects.isNullOrEmpty(digestHash), ExtRunningError.STATE_CHECK_ERROR.message("删除失败"));
        HttpServletResponse result = registry.deleteImage(repository, digestHash);
        return HttpStatus.resolve(result.getStatus()).is2xxSuccessful();
    }

    @Override
    public String pushImage(InputStream file) {
        LoadResponseItem item;
        try {
            LoadImageCmd cmd = docker.loadImageCmd(file);
            item = cmd.exec();
            List<Image> images = docker.listImagesCmd().withImageNameFilter(item.getImageName()).exec();
            Assert.isTrue(!Objects.isNullOrEmpty(images), ExtRunningError.STATE_CHECK_ERROR.message("加载镜像失败,请确保镜像正确并重试"));
        } catch (Exception e) {
            log.error("Load镜像失败", e);
            throw new BusinessException(RunningError.STATE_CHECK_ERROR.message("Load 镜像失败,请确保镜像正确并重试"));
        }

        try {
            PushImageCmd cmd = docker.pushImageCmd(item.getImageName());
            cmd.exec(new PushImageResultCallback() {
                @Override
                public void onComplete() {
                    log.info("PUSH SUCCESS IMAGES = {}", item.getImageName());
                    docker.removeImageCmd(item.getImageName());
                    log.info("CLEAN IMAGE TMP");
                }
            });
            return item.getImageName();
        } catch (Exception e) {
            log.error("Push镜像失败", e);
            throw new BusinessException(RunningError.STATE_CHECK_ERROR.message("Push 镜像失败,请确保镜像正确并重试"));
        }
    }

}