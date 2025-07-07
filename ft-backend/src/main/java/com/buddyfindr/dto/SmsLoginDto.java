package com.buddyfindr.dto;

import lombok.Data;

@Data
public class SmsLoginDto {
    private String zone;
    private String phone;
    private String vcode;
    private String pid;
} 