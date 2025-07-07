package com.buddyfindr.service.impl;

import com.buddyfindr.dto.ChatMessageDto;
import com.buddyfindr.entity.ChatMessage;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.repository.ChatMessageRepository;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final PetRepository petRepository;

    @Override
    public List<ChatMessageDto> getDialog(Long senderPetId, Long recipientPetId, int size) {
        Pet senderPet = petRepository.findById(senderPetId).orElseThrow();
        Pet recipientPet = petRepository.findById(recipientPetId).orElseThrow();
        return chatMessageRepository.findBySenderPetAndRecipientPetOrderByTsDesc(senderPet, recipientPet)
                .stream().limit(size).map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getChatList(Long petId, int size) {
        Pet pet = petRepository.findById(petId).orElseThrow();
        return chatMessageRepository.findBySenderPetOrRecipientPetOrderByTsDesc(pet, pet)
                .stream().limit(size).map(this::toDto).collect(Collectors.toList());
    }

    private ChatMessageDto toDto(ChatMessage msg) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(msg.getId());
        dto.setSenderId(msg.getSender().getId());
        dto.setSenderPetId(msg.getSenderPet().getId());
        dto.setRecipientId(msg.getRecipient().getId());
        dto.setRecipientPetId(msg.getRecipientPet().getId());
        dto.setMsgType(msg.getMsgType());
        dto.setMsg(msg.getMsg());
        dto.setSend(msg.getSend());
        dto.setTs(msg.getTs());
        return dto;
    }
} 