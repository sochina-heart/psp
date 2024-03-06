package com.sochina.stream.rocketmq.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class DemoMessage implements Serializable {
    private static final long serialVersionUID = -761662493627579882L;
    private String id;
}
