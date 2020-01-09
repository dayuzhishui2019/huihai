package com.github.dockerjava.api.model;

import lombok.Data;

/**
 * leus
 * <p>
 * Represents a load response stream item
 */
@Data
public class LoadResponseItem extends ResponseItem {

    private String tag;

    private String repository;

    private String imageName;

}
