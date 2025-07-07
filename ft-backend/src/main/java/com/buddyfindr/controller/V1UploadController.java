package com.buddyfindr.controller;

import com.buddyfindr.common.StandardApiResponse;
import com.buddyfindr.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/upload")
@RequiredArgsConstructor
public class V1UploadController {

    private final FileStorageUtil fileStorageUtil;

    @PostMapping("/avt/{uuid}")
    public ResponseEntity<?> uploadAvatar(@PathVariable String uuid,
                                          @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "File is empty"));
            }

            String path = fileStorageUtil.storeFile(file, "avt", uuid);
            Map<String, String> result = new HashMap<>();
            result.put("url", path);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPLOAD_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/lostpet/{uuid}")
    public ResponseEntity<?> uploadLostPet(@PathVariable String uuid,
                                           @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "File is empty"));
            }

            String path = fileStorageUtil.storeFile(file, "lostpet", uuid);
            Map<String, String> result = new HashMap<>();
            result.put("url", path);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPLOAD_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/pet/{uuid}")
    public ResponseEntity<?> uploadPet(@PathVariable String uuid,
                                       @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "File is empty"));
            }

            String path = fileStorageUtil.storeFile(file, "pet", uuid);
            Map<String, String> result = new HashMap<>();
            result.put("url", path);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPLOAD_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/cert/{uuid}")
    public ResponseEntity<?> uploadCert(@PathVariable String uuid,
                                        @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "File is empty"));
            }

            String path = fileStorageUtil.storeFile(file, "cert", uuid);
            Map<String, String> result = new HashMap<>();
            result.put("url", path);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPLOAD_FAILED", e.getMessage()));
        }
    }
} 