package com.buddyfindr.service.impl;

import com.buddyfindr.dto.DeviceDto;
import com.buddyfindr.dto.DeviceInfoDto;
import com.buddyfindr.entity.Device;
import com.buddyfindr.entity.Pet;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.DeviceRepository;
import com.buddyfindr.repository.PetRepository;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Override
    @Transactional
    public DeviceInfoDto addDevice(Long userId, DeviceDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Pet pet = null;
        if (dto.getPetId() != null) {
            pet = petRepository.findById(dto.getPetId()).orElse(null);
        }
        Device device = Device.builder()
                .id(dto.getId())
                .user(user)
                .pet(pet)
                .name(dto.getName())
                .avatar(dto.getAvatar())
                .mode(dto.getMode())
                .battery(dto.getBattery())
                .visible(dto.getVisible())
                .registered(dto.getRegistered())
                .network(dto.getNetwork())
                .version(dto.getVersion())
                .online(dto.getOnline())
                .build();
        device = deviceRepository.save(device);
        return toDeviceInfoDto(device);
    }

    @Override
    @Transactional
    public DeviceInfoDto updateDevice(Long userId, String deviceId, DeviceDto dto) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found"));
        if (!device.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        if (dto.getName() != null) device.setName(dto.getName());
        if (dto.getAvatar() != null) device.setAvatar(dto.getAvatar());
        if (dto.getPetId() != null) {
            Pet pet = petRepository.findById(dto.getPetId()).orElse(null);
            device.setPet(pet);
        }
        if (dto.getMode() != null) device.setMode(dto.getMode());
        if (dto.getBattery() != null) device.setBattery(dto.getBattery());
        if (dto.getVisible() != null) device.setVisible(dto.getVisible());
        if (dto.getRegistered() != null) device.setRegistered(dto.getRegistered());
        if (dto.getNetwork() != null) device.setNetwork(dto.getNetwork());
        if (dto.getVersion() != null) device.setVersion(dto.getVersion());
        if (dto.getOnline() != null) device.setOnline(dto.getOnline());
        device = deviceRepository.save(device);
        return toDeviceInfoDto(device);
    }

    @Override
    @Transactional
    public void deleteDevice(Long userId, String deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found"));
        if (!device.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        deviceRepository.delete(device);
    }

    @Override
    public DeviceInfoDto getDevice(Long userId, String deviceId) {
        Device device = deviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found"));
        if (!device.getUser().getId().equals(userId)) throw new RuntimeException("No permission");
        return toDeviceInfoDto(device);
    }

    @Override
    public List<DeviceInfoDto> listDevices(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return deviceRepository.findByUser(user).stream().map(this::toDeviceInfoDto).collect(Collectors.toList());
    }

    private DeviceInfoDto toDeviceInfoDto(Device device) {
        DeviceInfoDto dto = new DeviceInfoDto();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setAvatar(device.getAvatar());
        dto.setPetId(device.getPet() != null ? device.getPet().getId() : null);
        dto.setMode(device.getMode());
        dto.setBattery(device.getBattery());
        dto.setVisible(device.getVisible());
        dto.setRegistered(device.getRegistered());
        dto.setNetwork(device.getNetwork());
        dto.setVersion(device.getVersion());
        dto.setOnline(device.getOnline());
        return dto;
    }
} 