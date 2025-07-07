package com.buddyfindr.controller;

import com.buddyfindr.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {
    private final FileStorageUtil fileStorageUtil;

    @PostMapping("/avt/{uuid}")
    public ResponseEntity<?> uploadAvatar(@AuthenticationPrincipal UserDetails userDetails,
                                          @RequestParam("file") MultipartFile file,
                                          @PathVariable(required = false) String uuid) throws IOException {
        String path = fileStorageUtil.storeFile(file, "avt", uuid);
        Map<String, String> result = new HashMap<>();
        result.put("path", path);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/lostpet/{uuid}")
    public ResponseEntity<?> uploadLostPet(@AuthenticationPrincipal UserDetails userDetails,
                                           @RequestParam("file") MultipartFile file,
                                           @PathVariable(required = false) String uuid) throws IOException {
        String path = fileStorageUtil.storeFile(file, "lostpet", uuid);
        Map<String, String> result = new HashMap<>();
        result.put("path", path);
        return ResponseEntity.ok(result);
    }
} 