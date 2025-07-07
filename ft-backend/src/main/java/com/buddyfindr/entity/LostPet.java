package com.buddyfindr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lost_pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LostPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    private String title;
    @Column(length = 1024)
    private String info;
    private Integer reward;
    private LocalDateTime lostTs;
    private Double longitude;
    private Double latitude;
    @ElementCollection
    private List<String> photos;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
} 