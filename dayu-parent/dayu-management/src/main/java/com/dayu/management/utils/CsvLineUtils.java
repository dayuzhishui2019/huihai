package com.dayu.management.utils;

import com.google.common.base.Joiner;

import java.util.Objects;

public class CsvLineUtils {


    private static final Joiner join = Joiner.on(",");

    private CsvLineUtils() {
    }


    public static String join(Object... values) {
        String[] strings = new String[values.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = Objects.toString(values[i], "");
        }
        return join.join(strings);
    }
}
