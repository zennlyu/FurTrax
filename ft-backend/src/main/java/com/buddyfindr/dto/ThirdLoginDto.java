package com.buddyfindr.dto;

import lombok.Data;

@Data
public class ThirdLoginDto {
    private String thirdAccessToken;
    private Integer channel; // 1:FB, 2:GOOGLE
    private String avt;
    private String pid;
} 