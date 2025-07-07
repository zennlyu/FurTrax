package com.buddyfindr.service;

import com.buddyfindr.dto.LostPetV1Dto;
import com.buddyfindr.dto.LostPetV1InfoDto;
import java.util.List;

public interface LostPetV1Service {
    LostPetV1InfoDto publishLostPet(Long userId, LostPetV1Dto dto);
    LostPetV1InfoDto getLostPet(Long lostPetId);
    void deleteLostPet(Long userId, Long lostPetId);
    List<LostPetV1InfoDto> getUserLostPets(Long userId);
    List<LostPetV1InfoDto> getNearbyLostPets(Double longitude, Double latitude, Integer radius);
    boolean collectLostPet(Long userId, Long lostPetId);
    boolean uncollectLostPet(Long userId, Long lostPetId);
    List<LostPetV1InfoDto> getUserCollectedLostPets(Long userId);
} 