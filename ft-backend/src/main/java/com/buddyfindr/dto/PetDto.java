package com.buddyfindr.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PetDto {
    private String name;
    private String avatar;
    private LocalDate birthday;
    private Integer gender;
    private String breed;
    private Double weight;
    private String cert;
    private Integer family;
    private String status;
} 