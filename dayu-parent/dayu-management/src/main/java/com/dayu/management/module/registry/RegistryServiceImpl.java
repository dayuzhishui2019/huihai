package com.dayu.management.module.registry;

import com.dayu.management.module.registry.helper.RegistryClient;
import com.dayu.response.Assert;
import com.dayu.response.ExtRunningError;
import com.dayu.response.RunningError;
import com.dayu.response.exception.BusinessException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.leus.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;


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
        return Assert.notNull(registry.getTags(repository), RunningError.STATE_CHECK_ERROR.message("获取镜像Tag列表失败")).getTags();
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
    public boolean pushImage(String repository, String tag, InputStream file) {
        String imageName = String.format("%s:%s", repository, tag);
        try {
            docker.loadImageCmd(file).exec();
            List<Image> images = docker.listImagesCmd().withImageNameFilter(imageName).exec();
            Assert.isTrue(!Objects.isNullOrEmpty(images), ExtRunningError.STATE_CHECK_ERROR.message("加载镜像失败,请确保镜像正确并重试"));
        } catch (Exception e) {
            log.error("Load镜像失败", e);
            throw new BusinessException(RunningError.STATE_CHECK_ERROR.message("Load 镜像失败,请确保镜像正确并重试"));
        }

        try {
            PushImageCmd cmd = docker.pushImageCmd(imageName);
            boolean v = cmd.exec(new PushImageResultCallback() {
                @Override
                public void onComplete() {
                    log.info("PUSH SUCCESS IMAGES = {}:{}", repository, tag);
                }
            }).awaitCompletion(5, TimeUnit.MINUTES);
            docker.removeImageCmd(imageName).exec();
            return v;
        } catch (Exception e) {
            log.error("Push镜像失败", e);
            throw new BusinessException(RunningError.STATE_CHECK_ERROR.message("Push 镜像失败,请确保镜像正确并重试"));
        }
    }

    public static void main(String[] args) {
        System.out.println(new String(Base64.getDecoder().decode("XJAUMEO63nVb1aMJmmaGucNYMB30At0GMdf2hueyjJr0jLUktn-sCS8ujtRxzwVXFICGN9z_aiBk_arxn2ryjQ")));
    }
}
