package com.buddyfindr.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessageDto {
    private Long id;
    private Long senderId;
    private Long senderPetId;
    private Long recipientId;
    private Long recipientPetId;
    private String msgType;
    private String msg;
    private Boolean send;
    private LocalDateTime ts;
} 