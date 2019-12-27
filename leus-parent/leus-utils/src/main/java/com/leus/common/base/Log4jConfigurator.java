package com.leus.common.base;

import com.google.common.io.Resources;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * log4j日志初始化
 *
 * @author dyh
 */
public class Log4jConfigurator {
    private Log4jConfigurator() {
    }

    /**
     * 初始化
     *
     * @param path 工程下的相对路径
     */
    public static void configure(String path) {
        try {
            URL url = Resources.getResource(path);
            if (!Files.exists(Paths.get(url.toURI()))) {
                System.err.println("resource " + path + " not found");
                return;
            }
            PropertyConfigurator.configure(url);
        } catch (Exception e) {
        }

    }
}
