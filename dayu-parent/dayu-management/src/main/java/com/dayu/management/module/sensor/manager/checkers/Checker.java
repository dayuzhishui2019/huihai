package com.dayu.management.module.sensor.manager.checkers;

import java.util.List;

public interface Checker {

    boolean test(List<String> lineItem);
}
