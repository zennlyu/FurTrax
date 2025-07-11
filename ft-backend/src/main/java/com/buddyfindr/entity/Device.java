package com.buddyfindr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "devices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {
    @Id
    @Column(length = 64)
    private String id; // Device ID (cid)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(length = 64)
    private String name;

    @Column(length = 255)
    private String avatar;

    private Integer mode; // Working mode
    private Integer battery; // Battery level
    private Integer visible; // Visibility
    private Integer registered; // Registration status
    private Integer network; // Network type
    private String version; // Firmware version
    private Boolean online; // Online status
} 