package com.dayu.management.module.registry.helper;


import com.dayu.management.module.registry.fallback.RegistryFallback;
import com.dayu.management.module.registry.model.CataLog;
import com.dayu.management.module.registry.model.TagsList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@FeignClient(name = "registryClient", url = "http://registry:5453/v2", fallback = RegistryFallback.class)
public interface RegistryClient {

    @GetMapping("/_catalog")
    CataLog getCataLog();

    @GetMapping("/{repository}/tags/list")
    TagsList getTags(@PathVariable("repository") String repository);


    @GetMapping(value = "{repository}/manifests/{tag}")
    HttpServletResponse getManifests(@PathVariable String repository, @PathVariable String tag);


    @DeleteMapping(value = "{repository}/manifests/{digestHash}", produces = MediaType.APPLICATION_JSON_VALUE)
    HttpServletResponse deleteImage(@PathVariable String repository, @PathVariable String digestHash);

}
