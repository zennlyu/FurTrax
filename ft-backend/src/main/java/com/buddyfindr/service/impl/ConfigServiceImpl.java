package com.buddyfindr.service.impl;

import com.buddyfindr.dto.ConfigDto;
import com.buddyfindr.entity.Config;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.ConfigRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigRepository configRepository;
    private final UserRepository userRepository;

    @Override
    public ConfigDto getConfig(Long userId) {
        Config config = configRepository.findById(userId).orElse(new Config());
        ConfigDto dto = new ConfigDto();
        dto.setLang(config.getLang());
        dto.setMeasure(config.getMeasure());
        dto.setRecvOffical(config.getRecvOffical());
        dto.setRecvChat(config.getRecvChat());
        return dto;
    }

    @Override
    @Transactional
    public ConfigDto updateConfig(Long userId, ConfigDto dto) {
        Config config = configRepository.findById(userId).orElse(new Config());
        if (config.getUser() == null) {
            User user = userRepository.findById(userId).orElseThrow();
            config.setUser(user);
        }
        if (dto.getLang() != null) config.setLang(dto.getLang());
        if (dto.getMeasure() != null) config.setMeasure(dto.getMeasure());
        if (dto.getRecvOffical() != null) config.setRecvOffical(dto.getRecvOffical());
        if (dto.getRecvChat() != null) config.setRecvChat(dto.getRecvChat());
        configRepository.save(config);
        return getConfig(userId);
    }

    @Override
    @Transactional
    public boolean updateFcmToken(Long userId, String fcmToken) {
        try {
            Config config = configRepository.findById(userId).orElse(new Config());
            if (config.getUser() == null) {
                User user = userRepository.findById(userId).orElseThrow();
                config.setUser(user);
                config.setUserId(userId);
            }
            config.setFcmToken(fcmToken);
            configRepository.save(config);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
} 