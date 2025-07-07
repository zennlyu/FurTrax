package com.buddyfindr.service;

import com.buddyfindr.dto.PetDto;
import com.buddyfindr.dto.PetInfoDto;
import java.util.List;

public interface PetService {
    PetInfoDto addPet(Long userId, PetDto dto);
    PetInfoDto updatePet(Long userId, Long petId, PetDto dto);
    void deletePet(Long userId, Long petId);
    PetInfoDto getPet(Long userId, Long petId);
    List<PetInfoDto> listPets(Long userId);
} 