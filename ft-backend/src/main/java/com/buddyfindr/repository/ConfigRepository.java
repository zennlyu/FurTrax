package com.buddyfindr.repository;

import com.buddyfindr.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, Long> {
} 