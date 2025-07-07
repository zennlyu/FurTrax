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
    private String id; // 设备ID (cid)

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

    private Integer mode; // 工作模式
    private Integer battery; // 电量
    private Integer visible; // 可见性
    private Integer registered; // 注册状态
    private Integer network; // 网络类型
    private String version; // 固件版本
    private Boolean online; // 是否在线
} 