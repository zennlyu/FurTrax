package com.buddyfindr.service.impl;

import com.buddyfindr.dto.NotificationDto;
import com.buddyfindr.entity.Notification;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.NotificationRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public List<NotificationDto> getNotifications(Long userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow();
        return notificationRepository.findByUserOrderByTsDesc(user)
                .stream().skip((long) page * size).limit(size)
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public NotificationDto addNotification(Long userId, NotificationDto dto) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = Notification.builder()
                .user(user)
                .title(dto.getTitle())
                .text(dto.getText())
                .imageUrl(dto.getImageUrl())
                .build();
        notification = notificationRepository.save(notification);
        return toDto(notification);
    }

    private NotificationDto toDto(Notification n) {
        NotificationDto dto = new NotificationDto();
        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setText(n.getText());
        dto.setImageUrl(n.getImageUrl());
        dto.setTs(n.getTs());
        return dto;
    }
} 