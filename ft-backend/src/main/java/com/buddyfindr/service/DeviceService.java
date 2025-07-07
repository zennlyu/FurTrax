package com.buddyfindr.service;

import com.buddyfindr.dto.DeviceDto;
import com.buddyfindr.dto.DeviceInfoDto;
import java.util.List;

public interface DeviceService {
    DeviceInfoDto addDevice(Long userId, DeviceDto dto);
    DeviceInfoDto updateDevice(Long userId, String deviceId, DeviceDto dto);
    void deleteDevice(Long userId, String deviceId);
    DeviceInfoDto getDevice(Long userId, String deviceId);
    List<DeviceInfoDto> listDevices(Long userId);
} 