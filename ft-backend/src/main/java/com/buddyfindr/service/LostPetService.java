package com.buddyfindr.service;

import com.buddyfindr.dto.LostPetDto;
import com.buddyfindr.dto.LostPetInfoDto;
import java.util.List;

public interface LostPetService {
    LostPetInfoDto addLostPet(Long userId, LostPetDto dto);
    LostPetInfoDto updateLostPet(Long userId, Long lostPetId, LostPetDto dto);
    void deleteLostPet(Long userId, Long lostPetId);
    LostPetInfoDto getLostPet(Long userId, Long lostPetId);
    List<LostPetInfoDto> listMyLostPets(Long userId);
    List<LostPetInfoDto> listAllLostPets();
} 