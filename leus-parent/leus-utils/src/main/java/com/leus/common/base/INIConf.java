package com.leus.common.base;

import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.SubnodeConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * ini配置文件读取
 * Created by duanyihui on 2017/7/17.
 */
public class INIConf {

    private static final INIConfiguration configuration = new INIConfiguration();


    public static void bundle(String configPath) {
        try {
            configuration.read(new FileReader(Resources.getResource(configPath)));
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String name) {
        return configuration.getString(name);
    }

    public static String getString(String name, String defaultValue) {
        return configuration.getString(name, defaultValue);
    }

    public static Integer getInteger(String name) {
        return configuration.getInt(name);
    }

    public static Integer getInteger(String name, Integer defaultValue) {
        return configuration.getInteger(name, defaultValue);
    }

    public static Double getDouble(String name, Double defaultValue) {
        return configuration.getDouble(name, defaultValue);
    }

    public static Double getDouble(String name) {
        return configuration.getDouble(name);
    }

    public static Object getArray(String name, Class<?> cls) {
        return configuration.getArray(cls, name);
    }

    public static <T> List<T> getList(String name, Class<T> cls) {
        return configuration.getList(cls, name);
    }

    public static SubnodeConfiguration getSection(String name) {
        return configuration.getSection(name);
    }

    public static Boolean getBoolean(String name) {
        return configuration.getBoolean(name);
    }

    public static Boolean getBoolean(String name, Boolean defaultValue) {
        return configuration.getBoolean(name, defaultValue);
    }

}
