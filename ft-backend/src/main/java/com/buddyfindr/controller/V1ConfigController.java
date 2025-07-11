package com.buddyfindr.controller;

import com.buddyfindr.common.StandardApiResponse;
import com.buddyfindr.dto.ConfigDto;
import com.buddyfindr.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class V1ConfigController {

    private final ConfigService configService;

    @GetMapping("/conf/{userid}")
    public ResponseEntity<?> getConfig(@PathVariable("userid") Long userId) {
        try {
            ConfigDto config = configService.getConfig(userId);
            return ResponseEntity.ok(config);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_CONFIG_FAILED", e.getMessage()));
        }
    }

    @PutMapping("/conf/{userid}")
    public ResponseEntity<?> updateConfig(@PathVariable("userid") Long userId,
                                          @RequestBody ConfigDto dto) {
        try {
            ConfigDto updated = configService.updateConfig(userId, dto);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPDATE_CONFIG_FAILED", e.getMessage()));
        }
    }

    @PutMapping("/fcm_token")
    public ResponseEntity<?> updateFcmToken(@RequestBody Map<String, String> request,
                                            @RequestHeader("Authorization") String token) {
        try {
            String fcmToken = request.get("fcm_token");
            Long userId = extractUserIdFromToken(token);
            
            // Update FCM token
            boolean success = configService.updateFcmToken(userId, fcmToken);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPDATE_FCM_TOKEN_FAILED", e.getMessage()));
        }
    }

    private Long extractUserIdFromToken(String token) {
        // Simplified processing, should actually parse user ID from JWT
        return 1L;
    }
} 