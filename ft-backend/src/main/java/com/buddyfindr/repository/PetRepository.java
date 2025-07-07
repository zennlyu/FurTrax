package com.buddyfindr.repository;

import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);
} 