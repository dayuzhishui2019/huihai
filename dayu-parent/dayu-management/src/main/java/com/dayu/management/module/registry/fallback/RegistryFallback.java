package com.dayu.management.module.registry.fallback;

import com.dayu.management.module.registry.helper.RegistryClient;
import com.dayu.management.module.registry.model.CataLog;
import com.dayu.management.module.registry.model.TagsList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RegistryFallback implements RegistryClient {
    @Override
    public CataLog getCataLog() {
        log.error("请求CataLog出错");
        return null;
    }

    @Override
    public TagsList getTags(String repository) {
        log.error("请求taglist出错");
        return null;
    }

    @Override
    public HttpServletResponse getManifests(String repository, String tag) {
        log.error("请求getManifests出错");
        return null;
    }

    @Override
    public HttpServletResponse deleteImage(String repository, String digestHash) {
        log.error("请求deleteImage出错");
        return null;
    }


}
