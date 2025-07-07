package com.buddyfindr.service;

import com.buddyfindr.dto.ChatMessageDto;
import java.util.List;

public interface ChatMessageService {
    List<ChatMessageDto> getDialog(Long senderPetId, Long recipientPetId, int size);
    List<ChatMessageDto> getChatList(Long petId, int size);
} 