package com.buddyfindr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(length = 255)
    private String avatar;

    private LocalDate birthday;

    private Integer gender; // 1:公(未阉) 2:母(未绝育) 3:公(阉) 4:母(绝育)

    @Column(length = 64)
    private String breed;

    private Double weight;

    @Column(length = 255)
    private String cert;

    private Integer family; // 0:猫 1:狗

    @Column(length = 32)
    private String status;
} 