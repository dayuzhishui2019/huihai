package com.dayu.management.module.sensor.model.ext;

import com.dayu.management.module.sensor.model.derive.Camera;
import lombok.Data;

import java.util.List;

/**
 * 卡口
 */
@Data
public class Monitor {

    /**
     * 卡口ID
     */
    private String id;


    /**
     * 卡口关联的相机
     */
    private List<Camera> cameras;

}
