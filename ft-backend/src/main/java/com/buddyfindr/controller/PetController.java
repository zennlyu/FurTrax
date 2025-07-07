package com.buddyfindr.controller;

import com.buddyfindr.dto.PetDto;
import com.buddyfindr.dto.PetInfoDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.PetService;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PetInfoDto> addPet(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestBody PetDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        PetInfoDto pet = petService.addPet(user.getId(), dto);
        return ResponseEntity.ok(pet);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetInfoDto> updatePet(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long petId,
                                                @RequestBody PetDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        PetInfoDto pet = petService.updatePet(user.getId(), petId, dto);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(@AuthenticationPrincipal UserDetails userDetails,
                                          @PathVariable Long petId) {
        User user = userService.findByEmail(userDetails.getUsername());
        petService.deletePet(user.getId(), petId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetInfoDto> getPet(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable Long petId) {
        User user = userService.findByEmail(userDetails.getUsername());
        PetInfoDto pet = petService.getPet(user.getId(), petId);
        return ResponseEntity.ok(pet);
    }

    @GetMapping("/my")
    public ResponseEntity<List<PetInfoDto>> listPets(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<PetInfoDto> pets = petService.listPets(user.getId());
        return ResponseEntity.ok(pets);
    }
} 