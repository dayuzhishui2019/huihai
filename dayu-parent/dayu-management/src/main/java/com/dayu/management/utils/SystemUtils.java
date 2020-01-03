package com.dayu.management.utils;

import oshi.SystemInfo;

public class SystemUtils {


    private static final SystemInfo info = new SystemInfo();


    private SystemUtils() {

    }

    public static String getProcessID() {
        return info.getHardware().getProcessor().getProcessorIdentifier().getProcessorID();
    }


    public static String getSerialNumber() {
        return info.getHardware().getComputerSystem().getSerialNumber();
    }


    public static String getOSType() {
        return SystemInfo.getCurrentPlatformEnum().name();
    }

    public static void main(String[] args) {
        System.out.println(getSerialNumber());
    }
}
