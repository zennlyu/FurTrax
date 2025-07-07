package com.buddyfindr.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LostPetInfoDto {
    private Long id;
    private Long petId;
    private String title;
    private String info;
    private Integer reward;
    private LocalDateTime lostTs;
    private Double longitude;
    private Double latitude;
    private List<String> photos;
    private LocalDateTime createdAt;
} 