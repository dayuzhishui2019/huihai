package com.dayu.management.module.registry;

import java.io.InputStream;
import java.util.List;

public interface RegistryService {


    List<String> getImages();

    List<String> getTags(String repository);

    boolean deleteImage(String repository, String tag);

    String pushImage(InputStream file);

}
