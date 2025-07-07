package com.buddyfindr.service;

import com.buddyfindr.dto.PetV1Dto;
import com.buddyfindr.dto.PetV1InfoDto;
import java.util.List;

public interface PetV1Service {
    PetV1InfoDto addPet(Long userId, PetV1Dto dto);
    PetV1InfoDto getPet(Long petId);
    PetV1InfoDto updatePet(Long userId, Long petId, PetV1Dto dto);
    boolean updatePetStatus(Long userId, Long petId, String status);
    List<PetV1InfoDto> getUserPets(Long userId);
    void deletePet(Long userId, Long petId);
    List<String> getBreeds();
} 