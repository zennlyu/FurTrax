package com.buddyfindr.controller;

import com.buddyfindr.common.StandardApiResponse;
import com.buddyfindr.dto.*;
import com.buddyfindr.entity.User;
import com.buddyfindr.service.UserService;
import com.buddyfindr.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class V1UserController {

    private final UserService userService;
    private final VerificationCodeService verificationCodeService;

    @PostMapping("/check_email")
    public ResponseEntity<?> checkEmail(@RequestBody CheckEmailDto dto) {
        try {
            boolean available = !userService.existsByEmail(dto.getEmail());
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", available);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "INTERNAL_ERROR", "Failed to check email"));
        }
    }

    @PostMapping("/check_user")
    public ResponseEntity<?> checkUser(@RequestBody CheckUserDto dto) {
        try {
            boolean available = !userService.existsByUserId(dto.getUser());
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", available);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "INTERNAL_ERROR", "Failed to check user"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody EmailRegisterDto dto) {
        try {
            // 验证邮箱验证码
            if (!verificationCodeService.verifyEmailCode(dto.getEmail(), dto.getVcode())) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "INVALID_VCODE", "Invalid verification code"));
            }

            // 注册用户
            User user = userService.registerWithEmail(dto.getEmail(), dto.getPid());
            String token = userService.generateToken(user);
            String refreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("userid", user.getId().toString());
            result.put("access_token", token);
            result.put("refresh_token", refreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "REGISTRATION_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/login/vcode")
    public ResponseEntity<?> loginWithVcode(@RequestBody VcodeLoginDto dto) {
        try {
            // 验证邮箱验证码
            if (!verificationCodeService.verifyEmailCode(dto.getEmail(), dto.getVcode())) {
                return ResponseEntity.status(401)
                        .body(StandardApiResponse.error(401, "INVALID_VCODE", "Invalid verification code"));
            }

            User user = userService.findByEmail(dto.getEmail());
            if (user == null) {
                return ResponseEntity.status(404)
                        .body(StandardApiResponse.error(404, "USER_NOT_FOUND", "User not found"));
            }

            String token = userService.generateToken(user);
            String refreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("userid", user.getId());
            result.put("access_token", token);
            result.put("refresh_token", refreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "LOGIN_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto dto) {
        try {
            String token = userService.login(dto);
            User user = userService.findByEmail(dto.getEmail());
            String refreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("userid", user.getId().toString());
            result.put("access_token", token);
            result.put("refresh_token", refreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "LOGIN_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/zones")
    public ResponseEntity<?> getZones() {
        // 返回支持的地区列表
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> zones = List.of(
                Map.of("zone", "1", "en", "USA", "zh", "美国"),
                Map.of("zone", "86", "en", "China", "zh", "中国"),
                Map.of("zone", "81", "en", "Japan", "zh", "日本"),
                Map.of("zone", "82", "en", "Korea", "zh", "韩国"),
                Map.of("zone", "44", "en", "UK", "zh", "英国")
        );
        result.put("list", zones);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/sms/send")
    public ResponseEntity<?> sendSms(@RequestBody Map<String, String> request) {
        try {
            String zone = request.get("zone");
            String phone = request.get("phone");
            String pid = request.get("pid");
            String lang = request.get("lang");

            boolean success = verificationCodeService.sendSmsCode(zone, phone, pid, lang);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "SMS_SEND_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/sms/login")
    public ResponseEntity<?> smsLogin(@RequestBody SmsLoginDto dto) {
        try {
            // 验证短信验证码
            String fullPhone = dto.getZone() + dto.getPhone();
            if (!verificationCodeService.verifySmsCode(fullPhone, dto.getVcode())) {
                return ResponseEntity.status(401)
                        .body(StandardApiResponse.error(401, "INVALID_VCODE", "Invalid verification code"));
            }

            // 查找或创建用户
            User user = userService.findOrCreateByPhone(dto.getZone(), dto.getPhone(), dto.getPid());
            String token = userService.generateToken(user);
            String refreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("userid", user.getId());
            result.put("access_token", token);
            result.put("refresh_token", refreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "LOGIN_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/third_login")
    public ResponseEntity<?> thirdLogin(@RequestBody ThirdLoginDto dto) {
        try {
            // 验证第三方token并获取用户信息
            User user = userService.thirdPartyLogin(dto.getThirdAccessToken(), dto.getChannel(), dto.getAvt(), dto.getPid());
            String token = userService.generateToken(user);
            String refreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("userid", user.getId().toString());
            result.put("access_token", token);
            result.put("refresh_token", refreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "THIRD_LOGIN_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/vcode/login")
    public ResponseEntity<?> sendLoginVcode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String pid = request.get("pid");

            boolean success = verificationCodeService.sendEmailCode(email, pid, "login");
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "VCODE_SEND_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/vcode/reset_user_pwd")
    public ResponseEntity<?> sendResetPwdVcode(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String pid = request.get("pid");

            boolean success = verificationCodeService.sendEmailCode(email, pid, "reset_pwd");
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "VCODE_SEND_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refresh_token");
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "Refresh token is required"));
            }

            if (!userService.validateRefreshToken(refreshToken)) {
                return ResponseEntity.status(401)
                        .body(StandardApiResponse.error(401, "INVALID_TOKEN", "Invalid refresh token"));
            }

            User user = userService.refreshToken(refreshToken);
            String newAccessToken = userService.generateToken(user);
            String newRefreshToken = userService.generateRefreshToken(user);

            Map<String, Object> result = new HashMap<>();
            result.put("access_token", newAccessToken);
            result.put("refresh_token", newRefreshToken);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "REFRESH_FAILED", e.getMessage()));
        }
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userid) {
        try {
            Long userId = Long.parseLong(userid);
            UserInfoDto userInfo = userService.getUserInfo(userId);
            return ResponseEntity.ok(userInfo);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400)
                    .body(StandardApiResponse.error(400, "INVALID_USER_ID", "Invalid user ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(StandardApiResponse.error(404, "USER_NOT_FOUND", "User not found"));
        }
    }

    @PutMapping("/user/{userid}")
    public ResponseEntity<?> updateUserInfo(@PathVariable String userid, @RequestBody UserUpdateDto dto) {
        try {
            Long userId = Long.parseLong(userid);
            UserInfoDto updated = userService.updateUserInfo(userId, dto);
            return ResponseEntity.ok(updated);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400)
                    .body(StandardApiResponse.error(400, "INVALID_USER_ID", "Invalid user ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "UPDATE_FAILED", e.getMessage()));
        }
    }

    @DeleteMapping("/user/{userid}")
    public ResponseEntity<?> deleteUser(@PathVariable String userid) {
        try {
            Long userId = Long.parseLong(userid);
            userService.deleteUser(userId);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", true);
            return ResponseEntity.ok(result);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400)
                    .body(StandardApiResponse.error(400, "INVALID_USER_ID", "Invalid user ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "DELETE_FAILED", e.getMessage()));
        }
    }

    @PutMapping("/user/{userid}/pwd")
    public ResponseEntity<?> changePassword(@PathVariable String userid, @RequestBody Map<String, String> request) {
        try {
            Long userId = Long.parseLong(userid);
            String oldPassword = request.get("old_pwd");
            String newPassword = request.get("new_pwd");

            if (oldPassword == null || newPassword == null) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "Old and new passwords are required"));
            }

            boolean success = userService.changePassword(userId, oldPassword, newPassword);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            return ResponseEntity.ok(result);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(400)
                    .body(StandardApiResponse.error(400, "INVALID_USER_ID", "Invalid user ID format"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PASSWORD_CHANGE_FAILED", e.getMessage()));
        }
    }

    @PostMapping("/reset_user_pwd")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String vcode = request.get("vcode");
            String newPassword = request.get("new_pwd");

            if (email == null || vcode == null || newPassword == null) {
                return ResponseEntity.status(400)
                        .body(StandardApiResponse.error(400, "BAD_REQUEST", "Email, verification code and new password are required"));
            }

            // 验证邮箱验证码
            if (!verificationCodeService.verifyEmailCode(email, vcode)) {
                return ResponseEntity.status(401)
                        .body(StandardApiResponse.error(401, "INVALID_VCODE", "Invalid verification code"));
            }

            boolean success = userService.resetPassword(email, newPassword);
            Map<String, Boolean> result = new HashMap<>();
            result.put("ok", success);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(StandardApiResponse.error(500, "PASSWORD_RESET_FAILED", e.getMessage()));
        }
    }
} 