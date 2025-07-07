package com.buddyfindr.dto;

import lombok.Data;

@Data
public class DeviceNotifyDto {
    private String cid; // 设备ID
    private Integer seq;
    private Integer cmd; // 设备操作码
    private Integer code; // 0: 成功；其他：失败
    private String msg; // 附加消息
} 