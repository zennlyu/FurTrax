package com.buddyfindr.controller;

import com.buddyfindr.dto.RelationDto;
import com.buddyfindr.dto.PetSimpleInfoDto;
import com.buddyfindr.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/relation")
@RequiredArgsConstructor
public class RelationController {
    private final RelationService relationService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@RequestBody RelationDto dto) {
        relationService.follow(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Void> unfollow(@RequestBody RelationDto dto) {
        relationService.unfollow(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/block")
    public ResponseEntity<Void> block(@RequestBody RelationDto dto) {
        relationService.block(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unblock")
    public ResponseEntity<Void> unblock(@RequestBody RelationDto dto) {
        relationService.unblock(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/following/{petId}")
    public ResponseEntity<List<PetSimpleInfoDto>> getFollowing(@PathVariable Long petId) {
        return ResponseEntity.ok(relationService.getFollowing(petId));
    }

    @GetMapping("/followers/{petId}")
    public ResponseEntity<List<PetSimpleInfoDto>> getFollowers(@PathVariable Long petId) {
        return ResponseEntity.ok(relationService.getFollowers(petId));
    }

    @GetMapping("/blocked/{petId}")
    public ResponseEntity<List<PetSimpleInfoDto>> getBlocked(@PathVariable Long petId) {
        return ResponseEntity.ok(relationService.getBlocked(petId));
    }
} 