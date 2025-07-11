package com.buddyfindr.service.impl;

import com.buddyfindr.dto.UserRegisterDto;
import com.buddyfindr.dto.UserLoginDto;
import com.buddyfindr.dto.UserInfoDto;
import com.buddyfindr.dto.UserUpdateDto;
import com.buddyfindr.entity.User;
import com.buddyfindr.repository.UserRepository;
import com.buddyfindr.service.UserService;
import com.buddyfindr.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public User register(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .build();
        return userRepository.save(user);
    }

    @Override
    public String login(UserLoginDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserInfoDto getUserInfo(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return toUserInfoDto(user);
    }

    @Override
    @Transactional
    public UserInfoDto updateUserInfo(Long id, UserUpdateDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getAvatar() != null) user.setAvatar(dto.getAvatar());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getBirthday() != null) user.setBirthday(dto.getBirthday());
        userRepository.save(user);
        return toUserInfoDto(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUserId(String userId) {
        try {
            Long id = Long.parseLong(userId);
            return userRepository.existsById(id);
        } catch (NumberFormatException e) {
            return userRepository.findByEmail(userId).isPresent();
        }
    }

    @Override
    @Transactional
    public User registerWithEmail(String email, String pid) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .email(email)
                .name(email.split("@")[0]) // Use email prefix as default username
                .build();
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User findOrCreateByPhone(String zone, String phone, String pid) {
        String fullPhone = zone + phone;
        // Need to add phone field query, temporarily using email field to store phone number
        User user = userRepository.findByEmail(fullPhone).orElse(null);
        if (user == null) {
            user = User.builder()
                    .email(fullPhone) // Temporarily use email field to store phone number
                    .phone(fullPhone)
                    .name("User" + phone.substring(Math.max(0, phone.length() - 4)))
                    .build();
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    @Transactional
    public User thirdPartyLogin(String thirdAccessToken, Integer channel, String avt, String pid) {
        // Need to call third-party API to verify token and get user information
        // Temporarily simplified processing, using token as unique identifier
        String thirdPartyId = "third_" + channel + "_" + thirdAccessToken.substring(0, Math.min(10, thirdAccessToken.length()));
        
        User user = userRepository.findByEmail(thirdPartyId).orElse(null);
        if (user == null) {
            user = User.builder()
                    .email(thirdPartyId)
                    .name("Third-party User")
                    .avatar(avt)
                    .build();
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public String generateToken(User user) {
        return jwtUtil.generateToken(user);
    }

    @Override
    public String generateRefreshToken(User user) {
        return jwtUtil.generateRefreshToken(user);
    }

    @Override
    public User refreshToken(String refreshToken) {
        String email = jwtUtil.getEmailFromRefreshToken(refreshToken);
        return findByEmail(email);
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        return jwtUtil.validateRefreshToken(refreshToken);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check old password
        if (user.getPassword() != null && !passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        
        // Set new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    private UserInfoDto toUserInfoDto(User user) {
        UserInfoDto dto = new UserInfoDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setBirthday(user.getBirthday());
        return dto;
    }
} 