package com.dayu.management.module.sensor.manager;

import com.dayu.management.module.sensor.manager.checkers.Cause;
import com.dayu.management.module.sensor.manager.checkers.Checker;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class SensorChecker {

    private Multimap<String, Checker> checkerMap = HashMultimap.create();

    public void register(String named, Checker checker) {
        checkerMap.put(named, checker);
    }


    public Checkers getCheckers(String sensorType) {
        return new Checkers(checkerMap.get(sensorType));
    }


    public Checkers getStandingBookChecker() {
        return new Checkers(checkerMap.get("StandingBook"));
    }


    public class Checkers {

        private Collection<Checker> checkers;

        private Checkers(Collection<Checker> checkers) {
            this.checkers = checkers;
        }

        public Cause test(List<String> items) {
            for (Checker checker : checkers) {
                Cause cause = checker.test(items);
                if (!cause.isSuccess()) {
                    return cause;
                }
            }
            return Cause.success();
        }

    }

}
