package com.buddyfindr.dto;

import lombok.Data;
import java.util.List;

@Data
public class PetV1Dto {
    private String name;
    private String avt;
    private String birthday;
    private Integer gender;
    private String breed;
    private Double weight;
    private String cert;
    private Integer family;
    private List<String> photos;
} 