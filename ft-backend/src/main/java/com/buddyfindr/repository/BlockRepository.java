package com.buddyfindr.repository;

import com.buddyfindr.entity.Block;
import com.buddyfindr.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findByPet(Pet pet);
    void deleteByPetAndFriendPet(Pet pet, Pet friendPet);
    boolean existsByPetAndFriendPet(Pet pet, Pet friendPet);
} 