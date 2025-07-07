package com.buddyfindr.service.impl;

import com.buddyfindr.dto.PetDto;
import com.buddyfindr.dto.PetInfoDto;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PetInfoDto addPet(Long userId, PetDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Pet pet = Pet.builder()
                .user(user)
                .name(dto.getName())
                .avatar(dto.getAvatar())
                .birthday(dto.getBirthday())
                .gender(dto.getGender())
                .breed(dto.getBreed())
                .weight(dto.getWeight())
                .cert(dto.getCert())
                .family(dto.getFamily())
                .status(dto.getStatus())
                .build();
        pet = petRepository.save(pet);
        return toPetInfoDto(pet);
    }

    @Override
    @Transactional
    public PetInfoDto updatePet(Long userId, Long petId, PetDto dto) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        if (!pet.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        if (dto.getName() != null) pet.setName(dto.getName());
        if (dto.getAvatar() != null) pet.setAvatar(dto.getAvatar());
        if (dto.getBirthday() != null) pet.setBirthday(dto.getBirthday());
        if (dto.getGender() != null) pet.setGender(dto.getGender());
        if (dto.getBreed() != null) pet.setBreed(dto.getBreed());
        if (dto.getWeight() != null) pet.setWeight(dto.getWeight());
        if (dto.getCert() != null) pet.setCert(dto.getCert());
        if (dto.getFamily() != null) pet.setFamily(dto.getFamily());
        if (dto.getStatus() != null) pet.setStatus(dto.getStatus());
        pet = petRepository.save(pet);
        return toPetInfoDto(pet);
    }

    @Override
    @Transactional
    public void deletePet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        if (!pet.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        petRepository.delete(pet);
    }

    @Override
    public PetInfoDto getPet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        if (!pet.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        return toPetInfoDto(pet);
    }

    @Override
    public List<PetInfoDto> listPets(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return petRepository.findByUser(user).stream().map(this::toPetInfoDto).collect(Collectors.toList());
    }

    private PetInfoDto toPetInfoDto(Pet pet) {
        PetInfoDto dto = new PetInfoDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setAvatar(pet.getAvatar());
        dto.setBirthday(pet.getBirthday());
        dto.setGender(pet.getGender());
        dto.setBreed(pet.getBreed());
        dto.setWeight(pet.getWeight());
        dto.setCert(pet.getCert());
        dto.setFamily(pet.getFamily());
        dto.setStatus(pet.getStatus());
        return dto;
    }
} 