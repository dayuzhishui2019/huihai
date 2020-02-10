package com.dayu.management.module.task.model;

import lombok.Data;

import java.util.List;

@Data
public class BoxView {

    private Box box;

    private List<Task> tasks;

}
