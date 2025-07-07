package com.buddyfindr.controller;

import com.buddyfindr.dto.ChatMessageDto;
import com.buddyfindr.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/dialog")
    public ResponseEntity<List<ChatMessageDto>> getDialog(@RequestParam Long senderPetId,
                                                          @RequestParam Long recipientPetId,
                                                          @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(chatMessageService.getDialog(senderPetId, recipientPetId, size));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatMessageDto>> getChatList(@RequestParam Long petId,
                                                            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(chatMessageService.getChatList(petId, size));
    }
} 