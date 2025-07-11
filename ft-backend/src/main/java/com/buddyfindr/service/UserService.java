package com.buddyfindr.service;

import com.buddyfindr.dto.UserRegisterDto;
import com.buddyfindr.dto.UserLoginDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.dto.UserInfoDto;
import com.buddyfindr.dto.UserUpdateDto;

public interface UserService {
    User register(UserRegisterDto dto);
    String login(UserLoginDto dto);
    User findByEmail(String email);
    UserInfoDto getUserInfo(Long id);
    UserInfoDto updateUserInfo(Long id, UserUpdateDto dto);
    
    // New methods
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    User registerWithEmail(String email, String pid);
    User findOrCreateByPhone(String zone, String phone, String pid);
    User thirdPartyLogin(String thirdAccessToken, Integer channel, String avt, String pid);
    String generateToken(User user);
    String generateRefreshToken(User user);
    User refreshToken(String refreshToken);
    boolean validateRefreshToken(String refreshToken);
    void deleteUser(Long userId);
    boolean changePassword(Long userId, String oldPassword, String newPassword);
    boolean resetPassword(String email, String newPassword);
} 