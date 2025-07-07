package com.buddyfindr.repository;

import com.buddyfindr.entity.ChatMessage;
import com.buddyfindr.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderPetAndRecipientPetOrderByTsDesc(Pet senderPet, Pet recipientPet);
    List<ChatMessage> findBySenderPetOrRecipientPetOrderByTsDesc(Pet senderPet, Pet recipientPet);
} 