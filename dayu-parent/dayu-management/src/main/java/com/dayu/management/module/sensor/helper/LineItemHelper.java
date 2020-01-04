package com.dayu.management.module.sensor.helper;

import com.dayu.management.config.StandingBookIni;
import com.dayu.management.constant.StandingBook;
import com.google.common.base.Splitter;

import java.util.List;

public class LineItemHelper {

    private LineItemHelper() {
    }

    public static boolean testSubTypes(StandingBookIni ini, List<String> lineItem) {
        List<String> finalItems = ini.getSubTypes(lineItem.get(StandingBook.TYPE));
        List<String> subItems = Splitter.on(";").trimResults().omitEmptyStrings().splitToList(lineItem.get(StandingBook.SUB_TYPE));
        for (String subItem : subItems) {
            if (!finalItems.contains(subItem)) {
                return false;
            }
        }
        return true;
    }

}
