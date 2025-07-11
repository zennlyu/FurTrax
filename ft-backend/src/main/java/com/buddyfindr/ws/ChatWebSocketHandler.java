package com.buddyfindr.ws;

import com.buddyfindr.dto.ChatMessageDto;
import com.buddyfindr.entity.ChatMessage;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.ChatMessageRepository;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<Long, WebSocketSession> petSessionMap = new ConcurrentHashMap<>();

    @Value("/ws/chat")
    private String wsPath;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Handshake requires token and petId
        String token = getParam(session, "token");
        String petIdStr = getParam(session, "petId");
        if (token == null || petIdStr == null) {
            session.close();
            return;
        }
        String email = jwtUtil.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElse(null);
        Long petId = Long.valueOf(petIdStr);
        Pet pet = petRepository.findById(petId).orElse(null);
        if (user == null || pet == null) {
            session.close();
            return;
        }
        petSessionMap.put(petId, session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse message
        ChatMessageDto dto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);
        // Persist
        ChatMessage chatMsg = ChatMessage.builder()
                .sender(userRepository.findById(dto.getSenderId()).orElse(null))
                .senderPet(petRepository.findById(dto.getSenderPetId()).orElse(null))
                .recipient(userRepository.findById(dto.getRecipientId()).orElse(null))
                .recipientPet(petRepository.findById(dto.getRecipientPetId()).orElse(null))
                .msgType(dto.getMsgType())
                .msg(dto.getMsg())
                .send(true)
                .build();
        chatMsg = chatMessageRepository.save(chatMsg);
        dto.setId(chatMsg.getId());
        dto.setTs(chatMsg.getTs());
        // Push to recipient
        WebSocketSession recipientSession = petSessionMap.get(dto.getRecipientPetId());
        if (recipientSession != null && recipientSession.isOpen()) {
            recipientSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(dto)));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        petSessionMap.values().remove(session);
    }

    private String getParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return null;
    }
} 