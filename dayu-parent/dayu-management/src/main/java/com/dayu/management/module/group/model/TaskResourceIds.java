package com.dayu.management.module.group.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("资源ID")
@Data
public class TaskResourceIds {

    @ApiModelProperty("组织机构IDs")
    //组织机构IDs
    private List<String> parentIds;

    @ApiModelProperty("设备IDs")
    //设备IDs
    private List<String> nodeIds;


}
