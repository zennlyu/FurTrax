package com.buddyfindr.service.impl;

import com.buddyfindr.dto.PetV1Dto;
import com.buddyfindr.dto.PetV1InfoDto;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.PetV1Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetV1ServiceImpl implements PetV1Service {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PetV1InfoDto addPet(Long userId, PetV1Dto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = Pet.builder()
                .user(user)
                .name(dto.getName())
                .avatar(dto.getAvt())
                .birthday(dto.getBirthday() != null ? LocalDate.parse(dto.getBirthday()) : null)
                .gender(dto.getGender())
                .breed(dto.getBreed())
                .weight(dto.getWeight())
                .cert(dto.getCert())
                .family(dto.getFamily())
                .status("normal")
                .build();

        pet = petRepository.save(pet);
        return toPetV1InfoDto(pet);
    }

    @Override
    public PetV1InfoDto getPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        return toPetV1InfoDto(pet);
    }

    @Override
    @Transactional
    public PetV1InfoDto updatePet(Long userId, Long petId, PetV1Dto dto) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // Verify pet owner
        if (!pet.getUser().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can update this pet");
        }

        // Update fields
        if (dto.getName() != null) pet.setName(dto.getName());
        if (dto.getAvt() != null) pet.setAvatar(dto.getAvt());
        if (dto.getBirthday() != null) pet.setBirthday(LocalDate.parse(dto.getBirthday()));
        if (dto.getGender() != null) pet.setGender(dto.getGender());
        if (dto.getBreed() != null) pet.setBreed(dto.getBreed());
        if (dto.getWeight() != null) pet.setWeight(dto.getWeight());
        if (dto.getCert() != null) pet.setCert(dto.getCert());
        if (dto.getFamily() != null) pet.setFamily(dto.getFamily());

        pet = petRepository.save(pet);
        return toPetV1InfoDto(pet);
    }

    @Override
    @Transactional
    public boolean updatePetStatus(Long userId, Long petId, String status) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // Verify pet owner
        if (!pet.getUser().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can update this pet");
        }

        pet.setStatus(status);
        petRepository.save(pet);
        return true;
    }

    @Override
    public List<PetV1InfoDto> getUserPets(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return petRepository.findByUser(user)
                .stream()
                .map(this::toPetV1InfoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deletePet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        // Verify pet owner
        if (!pet.getUser().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can delete this pet");
        }

        petRepository.delete(pet);
    }

    @Override
    public List<String> getBreeds() {
        // Return common pet breeds
        return Arrays.asList(
                "Golden Retriever", "Labrador", "Husky", "Samoyed", "Shiba Inu", "Teddy", "Border Collie", "German Shepherd", "Bichon", "Pomeranian",
                "British Shorthair", "American Shorthair", "Siamese", "Ragdoll", "Persian", "Scottish Fold", "Garfield", "Tabby", "Orange Cat", "Calico"
        );
    }

    private PetV1InfoDto toPetV1InfoDto(Pet pet) {
        PetV1InfoDto dto = new PetV1InfoDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setAvt(pet.getAvatar());
        dto.setBirthday(pet.getBirthday() != null ? pet.getBirthday().toString() : null);
        dto.setGender(pet.getGender());
        dto.setBreed(pet.getBreed());
        dto.setWeight(pet.getWeight());
        dto.setCert(pet.getCert());
        dto.setFamily(pet.getFamily());
        dto.setStatus(pet.getStatus());
        dto.setUserid(pet.getUser().getId());
        dto.setUsername(pet.getUser().getName());
        dto.setUser_avt(pet.getUser().getAvatar());
        
        // Temporarily use empty photos array, can be extended later
        dto.setPhotos(Arrays.asList());
        
        // Set timestamps (temporarily use current time)
        long currentTs = System.currentTimeMillis() / 1000;
        dto.setCreated_ts(currentTs);
        dto.setUpdated_ts(currentTs);
        
        return dto;
    }
} 