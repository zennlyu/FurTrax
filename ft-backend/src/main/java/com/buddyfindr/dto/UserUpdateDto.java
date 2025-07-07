package com.buddyfindr.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserUpdateDto {
    private String name;
    private String avatar;
    private String phone;
    private LocalDate birthday;
} 