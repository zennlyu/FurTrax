package com.buddyfindr.controller;

import com.buddyfindr.dto.ConfigDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.ConfigService;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
@RequiredArgsConstructor
public class ConfigController {
    private final ConfigService configService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ConfigDto> getConfig(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(configService.getConfig(user.getId()));
    }

    @PutMapping
    public ResponseEntity<ConfigDto> updateConfig(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody ConfigDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(configService.updateConfig(user.getId(), dto));
    }
} 