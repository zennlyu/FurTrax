package com.buddyfindr.controller;

import com.buddyfindr.common.StandardApiResponse;
import com.buddyfindr.dto.LostPetV1Dto;
import com.buddyfindr.dto.LostPetV1InfoDto;
import com.buddyfindr.service.LostPetV1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class V1LostPetController {

    private final LostPetV1Service lostPetV1Service;

    @PostMapping("/lost")
    public ResponseEntity<?> publishLostPet(@RequestBody LostPetV1Dto dto,
                                            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            
            LostPetV1InfoDto lostPet = lostPetV1Service.publishLostPet(userId, dto);
            return ResponseEntity.ok(lostPet);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PUBLISH_LOST_PET_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/lost/{pet_id}")
    public ResponseEntity<?> getLostPet(@PathVariable("pet_id") Long petId) {
        try {
            LostPetV1InfoDto lostPet = lostPetV1Service.getLostPet(petId);
            return ResponseEntity.ok(lostPet);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(StandardApiResponse.error(404, "LOST_PET_NOT_FOUND", "Lost pet not found"));
        }
    }

    @DeleteMapping("/lost/{pet_id}")
    public ResponseEntity<?> deleteLostPet(@PathVariable("pet_id") Long petId,
                                           @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            
            lostPetV1Service.deleteLostPet(userId, petId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", true);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "DELETE_LOST_PET_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/lost_pets")
    public ResponseEntity<?> getUserLostPets(@RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            
            List<LostPetV1InfoDto> lostPets = lostPetV1Service.getUserLostPets(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("list", lostPets);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_USER_LOST_PETS_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/neighbours/lost_pets")
    public ResponseEntity<?> getNearbyLostPets(@RequestParam("lo") Double longitude,
                                               @RequestParam("la") Double latitude,
                                               @RequestParam(value = "radius", defaultValue = "5") Integer radius) {
        try {
            List<LostPetV1InfoDto> lostPets = lostPetV1Service.getNearbyLostPets(longitude, latitude, radius);
            Map<String, Object> result = new HashMap<>();
            result.put("list", lostPets);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_NEARBY_LOST_PETS_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/lost_collection")
    public ResponseEntity<?> collectLostPet(@RequestBody Map<String, Long> request,
                                            @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            Long lostPetId = request.get("lost_pet_id");
            
            boolean success = lostPetV1Service.collectLostPet(userId, lostPetId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "COLLECT_LOST_PET_FAILED", e.getMessage()));
        }
    }

    @DeleteMapping("/lost_collection/{userid}/{pet_id}")
    public ResponseEntity<?> uncollectLostPet(@PathVariable("userid") Long userId,
                                              @PathVariable("pet_id") Long petId,
                                              @RequestHeader("Authorization") String token) {
        try {
            // 验证用户权限
            Long tokenUserId = extractUserIdFromToken(token);
            if (!tokenUserId.equals(userId)) {
                return ResponseEntity.status(403)
                        .body(StandardApiResponse.error(403, "FORBIDDEN", "Access denied"));
            }
            
            boolean success = lostPetV1Service.uncollectLostPet(userId, petId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UNCOLLECT_LOST_PET_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/lost_collection/{userid}")
    public ResponseEntity<?> getUserCollectedLostPets(@PathVariable("userid") Long userId) {
        try {
            List<LostPetV1InfoDto> lostPets = lostPetV1Service.getUserCollectedLostPets(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("list", lostPets);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_COLLECTED_LOST_PETS_FAILED", e.getMessage()));
        }
    }

    private Long extractUserIdFromToken(String token) {
        // 简化处理，实际应该从JWT中解析用户ID
        return 1L;
    }
} 