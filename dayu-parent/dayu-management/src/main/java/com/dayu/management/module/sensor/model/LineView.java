package com.dayu.management.module.sensor.model;

import lombok.Data;

@Data
public class LineView {


    //国标ID
    private String gid;

    //老记录
    private String olderLine;

    //新记录
    private String newerLine;

    public LineView(String gid, String olderLine, String newerLine) {
        this.gid = gid;
        this.olderLine = olderLine;
        this.newerLine = newerLine;
    }
}
