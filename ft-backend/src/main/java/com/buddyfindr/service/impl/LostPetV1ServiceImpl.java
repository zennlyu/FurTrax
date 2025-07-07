package com.buddyfindr.service.impl;

import com.buddyfindr.dto.LostPetV1Dto;
import com.buddyfindr.dto.LostPetV1InfoDto;
import com.buddyfindr.entity.LostPet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.LostPetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.LostPetV1Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LostPetV1ServiceImpl implements LostPetV1Service {

    private final LostPetRepository lostPetRepository;
    private final UserRepository userRepository;

    @Override
    public LostPetV1InfoDto publishLostPet(Long userId, LostPetV1Dto dto) {
        log.info("Publishing lost pet for user: {}", userId);
        
        // 查找用户
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        // 创建走失宠物记录
        LostPet lostPet = LostPet.builder()
                .owner(user)
                .title(dto.getTitle())
                .info(dto.getInfo())
                .reward(dto.getReward())
                .lostTs(dto.getLost_ts() != null ? 
                    LocalDateTime.ofEpochSecond(dto.getLost_ts(), 0, ZoneOffset.UTC) : 
                    LocalDateTime.now())
                .latitude(dto.getLa())
                .longitude(dto.getLo())
                .photos(dto.getPhotos())
                .build();

        // 保存到数据库
        LostPet savedLostPet = lostPetRepository.save(lostPet);
        
        log.info("Lost pet published successfully with ID: {}", savedLostPet.getId());
        
        return convertToInfoDto(savedLostPet);
    }

    @Override
    @Transactional(readOnly = true)
    public LostPetV1InfoDto getLostPet(Long lostPetId) {
        log.info("Getting lost pet info for ID: {}", lostPetId);
        
        LostPet lostPet = lostPetRepository.findById(lostPetId)
                .orElseThrow(() -> new RuntimeException("Lost pet not found: " + lostPetId));
        
        return convertToInfoDto(lostPet);
    }

    @Override
    public void deleteLostPet(Long userId, Long lostPetId) {
        log.info("Deleting lost pet {} for user: {}", lostPetId, userId);
        
        LostPet lostPet = lostPetRepository.findById(lostPetId)
                .orElseThrow(() -> new RuntimeException("Lost pet not found: " + lostPetId));
        
        // 验证用户权限
        if (!lostPet.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this lost pet");
        }
        
        lostPetRepository.delete(lostPet);
        log.info("Lost pet deleted successfully: {}", lostPetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPetV1InfoDto> getUserLostPets(Long userId) {
        log.info("Getting lost pets for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        List<LostPet> lostPets = lostPetRepository.findByOwnerOrderByCreatedAtDesc(user);
        
        return lostPets.stream()
                .map(this::convertToInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPetV1InfoDto> getNearbyLostPets(Double longitude, Double latitude, Integer radius) {
        log.info("Getting nearby lost pets for location: {}, {} within {} km", latitude, longitude, radius);
        
        // 获取所有走失宠物，然后过滤距离
        List<LostPet> allLostPets = lostPetRepository.findAllByOrderByCreatedAtDesc();
        
        return allLostPets.stream()
                .filter(pet -> pet.getLatitude() != null && pet.getLongitude() != null)
                .filter(pet -> calculateDistance(latitude, longitude, pet.getLatitude(), pet.getLongitude()) <= radius)
                .map(this::convertToInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean collectLostPet(Long userId, Long lostPetId) {
        log.info("User {} collecting lost pet: {}", userId, lostPetId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        
        LostPet lostPet = lostPetRepository.findById(lostPetId)
                .orElseThrow(() -> new RuntimeException("Lost pet not found: " + lostPetId));
        
        // 这里可以实现收藏逻辑，现在简单返回true
        return true;
    }

    @Override
    public boolean uncollectLostPet(Long userId, Long lostPetId) {
        log.info("User {} uncollecting lost pet: {}", userId, lostPetId);
        
        // 这里实现取消收藏逻辑
        // 现在简单返回true
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LostPetV1InfoDto> getUserCollectedLostPets(Long userId) {
        log.info("Getting collected lost pets for user: {}", userId);
        
        // 这里应该返回用户收藏的走失宠物
        // 现在简单返回空列表
        return List.of();
    }

    /**
     * 转换为DTO
     */
    private LostPetV1InfoDto convertToInfoDto(LostPet lostPet) {
        LostPetV1InfoDto dto = new LostPetV1InfoDto();
        dto.setId(lostPet.getId());
        dto.setPet_id(lostPet.getPet() != null ? lostPet.getPet().getId() : null);
        dto.setOwner(String.valueOf(lostPet.getOwner().getId()));
        dto.setZone(""); // 默认空值
        dto.setPhone(""); // 默认空值
        dto.setEmail(""); // 默认空值
        dto.setAvt(lostPet.getOwner().getAvatar());
        dto.setName(lostPet.getOwner().getName());
        dto.setPhotos(lostPet.getPhotos());
        dto.setTitle(lostPet.getTitle());
        dto.setInfo(lostPet.getInfo());
        dto.setLost_ts(lostPet.getLostTs() != null ? 
            lostPet.getLostTs().toEpochSecond(ZoneOffset.UTC) : null);
        dto.setLo(lostPet.getLongitude());
        dto.setLa(lostPet.getLatitude());
        dto.setReward(lostPet.getReward());
        dto.setUserid(lostPet.getOwner().getId());
        dto.setUser_avt(lostPet.getOwner().getAvatar());
        dto.setCreated_ts(lostPet.getCreatedAt() != null ? 
            lostPet.getCreatedAt().toEpochSecond(ZoneOffset.UTC) : null);
        dto.setUpdated_ts(lostPet.getCreatedAt() != null ? 
            lostPet.getCreatedAt().toEpochSecond(ZoneOffset.UTC) : null);
        dto.setStatus("0"); // 默认丢失中
        
        return dto;
    }

    /**
     * 计算两点之间的距离（单位：公里）
     */
    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return Double.MAX_VALUE;
        }
        
        final int R = 6371; // 地球半径（公里）
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
} 