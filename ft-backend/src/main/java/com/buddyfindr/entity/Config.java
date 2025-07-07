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
    private Integer measure; // 0: 英制+磅; 1: 米+千克
    private Integer recvOffical; // 0: 不接受；1: 接受
    private Integer recvChat; // 0: 不接受；1: 接受
    private String fcmToken; // FCM推送token
} 