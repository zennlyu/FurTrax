package com.buddyfindr.controller;

import com.buddyfindr.dto.NotificationDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.NotificationService;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getNotifications(@AuthenticationPrincipal UserDetails userDetails,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.getNotifications(user.getId(), page, size));
    }

    @PostMapping
    public ResponseEntity<NotificationDto> addNotification(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody NotificationDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(notificationService.addNotification(user.getId(), dto));
    }
} 