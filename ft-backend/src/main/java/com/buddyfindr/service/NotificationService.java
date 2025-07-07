package com.buddyfindr.service;

import com.buddyfindr.dto.NotificationDto;
import java.util.List;

public interface NotificationService {
    List<NotificationDto> getNotifications(Long userId, int page, int size);
    NotificationDto addNotification(Long userId, NotificationDto dto);
} 