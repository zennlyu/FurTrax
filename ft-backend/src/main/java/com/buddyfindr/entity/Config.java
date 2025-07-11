package com.buddyfindr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Config {
    @Id
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String lang; // en_US, zh_CN
    private Integer measure; // 0: Imperial+pounds; 1: Metric+kilograms
    private Integer recvOffical; // 0: Not accepted; 1: Accepted
    private Integer recvChat; // 0: Not accepted; 1: Accepted
    private String fcmToken; // FCM push token
} 