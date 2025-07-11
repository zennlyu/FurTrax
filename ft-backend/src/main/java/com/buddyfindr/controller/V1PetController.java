package com.buddyfindr.controller;

import com.buddyfindr.common.StandardApiResponse;
import com.buddyfindr.dto.PetV1Dto;
import com.buddyfindr.dto.PetV1InfoDto;
import com.buddyfindr.service.PetV1Service;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class V1PetController {

    private final PetV1Service petV1Service;

    @PostMapping("/pet")
    public ResponseEntity<?> addPet(@RequestBody PetV1Dto dto, @RequestHeader("Authorization") String token) {
        try {
            // Get user ID from token (simplified processing)
            Long userId = extractUserIdFromToken(token);
            
            PetV1InfoDto pet = petV1Service.addPet(userId, dto);
            return ResponseEntity.ok(pet);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PET_ADD_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/pet/{pet_id}")
    public ResponseEntity<?> getPet(@PathVariable("pet_id") Long petId) {
        try {
            PetV1InfoDto pet = petV1Service.getPet(petId);
            return ResponseEntity.ok(pet);
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(StandardApiResponse.error(404, "PET_NOT_FOUND", "Pet not found"));
        }
    }

    @PutMapping("/pet/{pet_id}")
    public ResponseEntity<?> updatePet(@PathVariable("pet_id") Long petId, 
                                       @RequestBody PetV1Dto dto,
                                       @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            
            PetV1InfoDto pet = petV1Service.updatePet(userId, petId, dto);
            return ResponseEntity.ok(pet);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PET_UPDATE_FAILED", e.getMessage()));
        }
    }

    @PutMapping("/pets/{pet_id}/status")
    public ResponseEntity<?> updatePetStatus(@PathVariable("pet_id") Long petId,
                                             @RequestBody Map<String, String> request,
                                             @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            String status = request.get("status");
            
            boolean success = petV1Service.updatePetStatus(userId, petId, status);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "STATUS_UPDATE_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/pets/{userid}")
    public ResponseEntity<?> getUserPets(@PathVariable("userid") Long userId) {
        try {
            List<PetV1InfoDto> pets = petV1Service.getUserPets(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("list", pets);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_PETS_FAILED", e.getMessage()));
        }
    }

    @DeleteMapping("/pets/{pet_id}")
    public ResponseEntity<?> deletePet(@PathVariable("pet_id") Long petId,
                                       @RequestHeader("Authorization") String token) {
        try {
            Long userId = extractUserIdFromToken(token);
            
            petV1Service.deletePet(userId, petId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", true);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PET_DELETE_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/breeds")
    public ResponseEntity<?> getBreeds() {
        try {
            List<String> breeds = petV1Service.getBreeds();
            Map<String, Object> result = new HashMap<>();
            result.put("list", breeds);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "GET_BREEDS_FAILED", e.getMessage()));
        }
    }

    private Long extractUserIdFromToken(String token) {
        // Simplified processing, should actually parse user ID from JWT
        // Temporarily return a default value, need to integrate with JWT utility class
        return 1L;
    }
} 