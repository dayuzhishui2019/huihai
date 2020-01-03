package com.dayu.management.module.sensor.manager;

import com.dayu.management.module.sensor.manager.checkers.Checker;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SensorChecker {

    private Multimap<String, Checker> checkerMap = HashMultimap.create();

    public void register(String named, Checker checker) {
        checkerMap.put(named, checker);
    }


    public Checkers getCheckers(String sensorType) {
        return new Checkers(checkerMap.get(sensorType));
    }


    public class Checkers {

        private Collection<Checker> checkers;

        private Checkers(Collection<Checker> checkers) {
            this.checkers = checkers;
        }

        public boolean test(String line) {
            return checkers.stream().allMatch(checker -> checker.test(line));
        }

    }

}
