package com.buddyfindr.dto;

import lombok.Data;

@Data
public class VcodeLoginDto {
    private String pid;
    private String email;
    private String vcode;
} 