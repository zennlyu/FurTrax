package com.buddyfindr.controller;

import com.buddyfindr.dto.LostPetDto;
import com.buddyfindr.dto.LostPetInfoDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.LostPetService;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lost")
@RequiredArgsConstructor
public class LostPetController {
    private final LostPetService lostPetService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<LostPetInfoDto> addLostPet(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody LostPetDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        LostPetInfoDto lostPet = lostPetService.addLostPet(user.getId(), dto);
        return ResponseEntity.ok(lostPet);
    }

    @PutMapping("/{lostPetId}")
    public ResponseEntity<LostPetInfoDto> updateLostPet(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable Long lostPetId,
                                                        @RequestBody LostPetDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        LostPetInfoDto lostPet = lostPetService.updateLostPet(user.getId(), lostPetId, dto);
        return ResponseEntity.ok(lostPet);
    }

    @DeleteMapping("/{lostPetId}")
    public ResponseEntity<Void> deleteLostPet(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable Long lostPetId) {
        User user = userService.findByEmail(userDetails.getUsername());
        lostPetService.deleteLostPet(user.getId(), lostPetId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{lostPetId}")
    public ResponseEntity<LostPetInfoDto> getLostPet(@AuthenticationPrincipal UserDetails userDetails,
                                                     @PathVariable Long lostPetId) {
        User user = userService.findByEmail(userDetails.getUsername());
        LostPetInfoDto lostPet = lostPetService.getLostPet(user.getId(), lostPetId);
        return ResponseEntity.ok(lostPet);
    }

    @GetMapping("/my")
    public ResponseEntity<List<LostPetInfoDto>> listMyLostPets(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<LostPetInfoDto> lostPets = lostPetService.listMyLostPets(user.getId());
        return ResponseEntity.ok(lostPets);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LostPetInfoDto>> listAllLostPets() {
        List<LostPetInfoDto> lostPets = lostPetService.listAllLostPets();
        return ResponseEntity.ok(lostPets);
    }
} 