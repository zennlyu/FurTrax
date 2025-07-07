package com.buddyfindr.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDto {
    private Long id;
    private String title;
    private String text;
    private String imageUrl;
    private LocalDateTime ts;
}