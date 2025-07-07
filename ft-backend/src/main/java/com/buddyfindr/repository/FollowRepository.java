package com.buddyfindr.repository;

import com.buddyfindr.entity.Follow;
import com.buddyfindr.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByPet(Pet pet);
    List<Follow> findByFriendPet(Pet friendPet);
    void deleteByPetAndFriendPet(Pet pet, Pet friendPet);
    boolean existsByPetAndFriendPet(Pet pet, Pet friendPet);
} 