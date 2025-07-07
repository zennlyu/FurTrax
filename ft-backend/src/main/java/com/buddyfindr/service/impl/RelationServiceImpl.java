package com.buddyfindr.service.impl;

import com.buddyfindr.dto.RelationDto;
import com.buddyfindr.dto.PetSimpleInfoDto;
import com.buddyfindr.entity.Block;
import com.buddyfindr.entity.Follow;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.repository.BlockRepository;
import com.buddyfindr.repository.FollowRepository;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements RelationService {
    private final FollowRepository followRepository;
    private final BlockRepository blockRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public void follow(RelationDto dto) {
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found"));
        Pet friendPet = petRepository.findById(dto.getFriendPetId()).orElseThrow(() -> new RuntimeException("Friend pet not found"));
        if (followRepository.existsByPetAndFriendPet(pet, friendPet)) return;
        Follow follow = Follow.builder().pet(pet).friendPet(friendPet).build();
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(RelationDto dto) {
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found"));
        Pet friendPet = petRepository.findById(dto.getFriendPetId()).orElseThrow(() -> new RuntimeException("Friend pet not found"));
        followRepository.deleteByPetAndFriendPet(pet, friendPet);
    }

    @Override
    @Transactional
    public void block(RelationDto dto) {
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found"));
        Pet friendPet = petRepository.findById(dto.getFriendPetId()).orElseThrow(() -> new RuntimeException("Friend pet not found"));
        if (blockRepository.existsByPetAndFriendPet(pet, friendPet)) return;
        Block block = Block.builder().pet(pet).friendPet(friendPet).build();
        blockRepository.save(block);
    }

    @Override
    @Transactional
    public void unblock(RelationDto dto) {
        Pet pet = petRepository.findById(dto.getPetId()).orElseThrow(() -> new RuntimeException("Pet not found"));
        Pet friendPet = petRepository.findById(dto.getFriendPetId()).orElseThrow(() -> new RuntimeException("Friend pet not found"));
        blockRepository.deleteByPetAndFriendPet(pet, friendPet);
    }

    @Override
    public List<PetSimpleInfoDto> getFollowing(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        return followRepository.findByPet(pet).stream().map(f -> toPetSimpleInfoDto(f.getFriendPet())).collect(Collectors.toList());
    }

    @Override
    public List<PetSimpleInfoDto> getFollowers(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        return followRepository.findByFriendPet(pet).stream().map(f -> toPetSimpleInfoDto(f.getPet())).collect(Collectors.toList());
    }

    @Override
    public List<PetSimpleInfoDto> getBlocked(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Pet not found"));
        return blockRepository.findByPet(pet).stream().map(b -> toPetSimpleInfoDto(b.getFriendPet())).collect(Collectors.toList());
    }

    private PetSimpleInfoDto toPetSimpleInfoDto(Pet pet) {
        PetSimpleInfoDto dto = new PetSimpleInfoDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setAvatar(pet.getAvatar());
        dto.setBreed(pet.getBreed());
        return dto;
    }
} 