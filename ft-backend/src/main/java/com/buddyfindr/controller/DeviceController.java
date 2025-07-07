package com.buddyfindr.controller;

import com.buddyfindr.dto.DeviceDto;
import com.buddyfindr.dto.DeviceInfoDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.DeviceService;
import com.buddyfindr.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/device")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<DeviceInfoDto> addDevice(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody DeviceDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        DeviceInfoDto device = deviceService.addDevice(user.getId(), dto);
        return ResponseEntity.ok(device);
    }

    @PutMapping("/{deviceId}")
    public ResponseEntity<DeviceInfoDto> updateDevice(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable String deviceId,
                                                      @RequestBody DeviceDto dto) {
        User user = userService.findByEmail(userDetails.getUsername());
        DeviceInfoDto device = deviceService.updateDevice(user.getId(), deviceId, dto);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> deleteDevice(@AuthenticationPrincipal UserDetails userDetails,
                                             @PathVariable String deviceId) {
        User user = userService.findByEmail(userDetails.getUsername());
        deviceService.deleteDevice(user.getId(), deviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceInfoDto> getDevice(@AuthenticationPrincipal UserDetails userDetails,
                                                   @PathVariable String deviceId) {
        User user = userService.findByEmail(userDetails.getUsername());
        DeviceInfoDto device = deviceService.getDevice(user.getId(), deviceId);
        return ResponseEntity.ok(device);
    }

    @GetMapping("/my")
    public ResponseEntity<List<DeviceInfoDto>> listDevices(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<DeviceInfoDto> devices = deviceService.listDevices(user.getId());
        return ResponseEntity.ok(devices);
    }
} 