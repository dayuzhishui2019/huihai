package com.dayu.management.module.sensor.manager.checkers;

import java.util.List;

public interface Checker {

    Cause test(List<String> lineItem);
}
