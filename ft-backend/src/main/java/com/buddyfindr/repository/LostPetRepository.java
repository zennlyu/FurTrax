package com.buddyfindr.repository;

import com.buddyfindr.entity.LostPet;
import com.buddyfindr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LostPetRepository extends JpaRepository<LostPet, Long> {
    List<LostPet> findByOwner(User owner);
    List<LostPet> findByOwnerOrderByCreatedAtDesc(User owner);
    List<LostPet> findAllByOrderByCreatedAtDesc();
} 