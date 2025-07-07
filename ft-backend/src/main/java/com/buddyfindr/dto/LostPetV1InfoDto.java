package com.buddyfindr.dto;

import lombok.Data;
import java.util.List;

@Data
public class LostPetV1InfoDto {
    private Long id;
    private Long pet_id;
    private String owner;
    private String zone;
    private String phone;
    private String email;
    private String avt;
    private String name;
    private List<String> photos;
    private String title;
    private String info;
    private Long lost_ts;
    private Double lo; // 经度
    private Double la; // 纬度
    private Integer reward;
    private Long userid;
    private String user_avt;
    private Long created_ts;
    private Long updated_ts;
    private String status; // 0: 丢失中, 1: 已找到
} 