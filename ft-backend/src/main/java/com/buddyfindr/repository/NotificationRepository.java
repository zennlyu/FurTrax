package com.buddyfindr.repository;

import com.buddyfindr.entity.Notification;
import com.buddyfindr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserOrderByTsDesc(User user);
} 