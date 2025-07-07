package com.buddyfindr.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    private String name;
} 