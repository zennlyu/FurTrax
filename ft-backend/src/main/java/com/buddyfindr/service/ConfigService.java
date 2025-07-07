package com.buddyfindr.service;

import com.buddyfindr.dto.ConfigDto;

public interface ConfigService {
    ConfigDto getConfig(Long userId);
    ConfigDto updateConfig(Long userId, ConfigDto dto);
    boolean updateFcmToken(Long userId, String fcmToken);
} 