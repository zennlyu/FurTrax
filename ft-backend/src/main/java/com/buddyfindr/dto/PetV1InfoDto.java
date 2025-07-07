package com.buddyfindr.dto;

import lombok.Data;
import java.util.List;

@Data
public class PetV1InfoDto {
    private Long id;
    private String name;
    private String avt;
    private String birthday;
    private Integer gender;
    private String breed;
    private Double weight;
    private String cert;
    private Integer family;
    private String status;
    private List<String> photos;
    private Long userid;
    private String username;
    private String user_avt;
    private Long created_ts;
    private Long updated_ts;
} 