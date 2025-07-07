package com.buddyfindr.dto;

import lombok.Data;

@Data
public class EmailRegisterDto {
    private String pid;
    private String email;
    private String vcode;
} 