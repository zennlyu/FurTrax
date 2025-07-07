package com.buddyfindr.service.impl;

import com.buddyfindr.dto.LostPetDto;
import com.buddyfindr.dto.LostPetInfoDto;
import com.buddyfindr.entity.LostPet;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.LostPetRepository;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.LostPetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostPetServiceImpl implements LostPetService {
    private final LostPetRepository lostPetRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public LostPetInfoDto addLostPet(Long userId, LostPetDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found"));
        LostPet lostPet = LostPet.builder()
                .owner(user)
                .pet(pet)
                .title(dto.getTitle())
                .info(dto.getInfo())
                .reward(dto.getReward())
                .lostTs(dto.getLostTs())
                .longitude(dto.getLongitude())
                .latitude(dto.getLatitude())
                .photos(dto.getPhotos())
                .build();
        lostPet = lostPetRepository.save(lostPet);
        return toLostPetInfoDto(lostPet);
    }

    @Override
    @Transactional
    public LostPetInfoDto updateLostPet(Long userId, Long lostPetId, LostPetDto dto) {
        LostPet lostPet = lostPetRepository.findById(lostPetId).orElseThrow(() -> new RuntimeException("LostPet not found"));
        if (!lostPet.getOwner().getId().equals(userId)) throw new RuntimeException("No permission");
        if (dto.getTitle() != null) lostPet.setTitle(dto.getTitle());
        if (dto.getInfo() != null) lostPet.setInfo(dto.getInfo());
        if (dto.getReward() != null) lostPet.setReward(dto.getReward());
        if (dto.getLostTs() != null) lostPet.setLostTs(dto.getLostTs());
        if (dto.getLongitude() != null) lostPet.setLongitude(dto.getLongitude());
        if (dto.getLatitude() != null) lostPet.setLatitude(dto.getLatitude());
        if (dto.getPhotos() != null) lostPet.setPhotos(dto.getPhotos());
        lostPet = lostPetRepository.save(lostPet);
        return toLostPetInfoDto(lostPet);
    }

    @Override
    @Transactional
    public void deleteLostPet(Long userId, Long lostPetId) {
        LostPet lostPet = lostPetRepository.findById(lostPetId).orElseThrow(() -> new RuntimeException("LostPet not found"));
        if (!lostPet.getOwner().getId().equals(userId)) throw new RuntimeException("No permission");
        lostPetRepository.delete(lostPet);
    }

    @Override
    public LostPetInfoDto getLostPet(Long userId, Long lostPetId) {
        LostPet lostPet = lostPetRepository.findById(lostPetId).orElseThrow(() -> new RuntimeException("LostPet not found"));
        if (!lostPet.getOwner().getId().equals(userId)) throw new RuntimeException("No permission");
        return toLostPetInfoDto(lostPet);
    }

    @Override
    public List<LostPetInfoDto> listMyLostPets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return lostPetRepository.findByOwner(user).stream().map(this::toLostPetInfoDto).collect(Collectors.toList());
    }

    @Override
    public List<LostPetInfoDto> listAllLostPets() {
        return lostPetRepository.findAll().stream().map(this::toLostPetInfoDto).collect(Collectors.toList());
    }

    private LostPetInfoDto toLostPetInfoDto(LostPet lostPet) {
        LostPetInfoDto dto = new LostPetInfoDto();
        dto.setId(lostPet.getId());
        dto.setPetId(lostPet.getPet().getId());
        dto.setTitle(lostPet.getTitle());
        dto.setInfo(lostPet.getInfo());
        dto.setReward(lostPet.getReward());
        dto.setLostTs(lostPet.getLostTs());
        dto.setLongitude(lostPet.getLongitude());
        dto.setLatitude(lostPet.getLatitude());
        dto.setPhotos(lostPet.getPhotos());
        dto.setCreatedAt(lostPet.getCreatedAt());
        return dto;
    }
} 