package com.dayu.management.module.sensor.helper;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.regex.Pattern;

public class SQLCheckers {
    private SQLCheckers() {
    }

    private static Pattern select = Pattern.compile("select .+ from .+ where .+ limit (\\d{1,3}|1000) offset \\d{0,6}|1000000");


    public static boolean select(String sql) {
        sql = Joiner.on(" ").join(Splitter.on(CharMatcher.whitespace()).omitEmptyStrings().trimResults().splitToList(sql)).toLowerCase();
        return select.matcher(sql).matches();
    }

}
