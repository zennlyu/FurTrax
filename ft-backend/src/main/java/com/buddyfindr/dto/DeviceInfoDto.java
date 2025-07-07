package com.buddyfindr.dto;

import lombok.Data;

@Data
public class DeviceInfoDto {
    private String id;
    private String name;
    private String avatar;
    private Long petId;
    private Integer mode;
    private Integer battery;
    private Integer visible;
    private Integer registered;
    private Integer network;
    private String version;
    private Boolean online;
} 