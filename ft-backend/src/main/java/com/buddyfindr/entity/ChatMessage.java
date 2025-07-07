package com.buddyfindr.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_pet_id", nullable = false)
    private Pet senderPet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_pet_id", nullable = false)
    private Pet recipientPet;

    @Column(length = 16)
    private String msgType; // text, image

    @Column(columnDefinition = "TEXT")
    private String msg; // 文本或图片URL

    private Boolean send; // true: 发送, false: 接收

    private LocalDateTime ts;

    @PrePersist
    public void prePersist() {
        ts = LocalDateTime.now();
    }
} 