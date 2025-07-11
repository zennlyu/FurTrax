package com.buddyfindr.controller;

import com.buddyfindr.dto.UserRegisterDto;
import com.buddyfindr.dto.UserLoginDto;
import com.buddyfindr.dto.UserInfoDto;
import com.buddyfindr.dto.UserUpdateDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegisterDto dto) {
        User user = userService.register(dto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDto dto) {
        String token = userService.login(dto);
        return ResponseEntity.ok().body(new AuthResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoDto> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        UserInfoDto dto = userService.getUserInfo(user.getId());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/me")
    public ResponseEntity<UserInfoDto> updateUserInfo(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody UserUpdateDto dto) {
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        UserInfoDto updated = userService.updateUserInfo(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }

            // Simple token response DTO
    record AuthResponse(String accessToken) {}
} 