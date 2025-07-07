package com.buddyfindr.repository;

import com.buddyfindr.entity.Device;
import com.buddyfindr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {
    List<Device> findByUser(User user);
} 