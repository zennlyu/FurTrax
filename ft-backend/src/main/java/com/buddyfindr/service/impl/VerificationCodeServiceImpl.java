package com.buddyfindr.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.buddyfindr.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender;

    @Value("${verification.code.length:6}")
    private int codeLength;

    @Value("${verification.code.expiration:300}")
    private long codeExpiration;

    @Value("${verification.code.mock-enabled:false}")
    private boolean mockEnabled;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${aliyun.sms.access-key-id:}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret:}")
    private String accessKeySecret;

    @Value("${aliyun.sms.sign-name:BuddyFindr}")
    private String signName;

    @Value("${aliyun.sms.template-code:}")
    private String templateCode;

    @Value("${aliyun.sms.enabled:true}")
    private boolean smsEnabled;

    private static final String EMAIL_CODE_PREFIX = "email_code:";
    private static final String SMS_CODE_PREFIX = "sms_code:";
    private static final String MOCK_CODE = "123456"; // 本地开发固定验证码

    @Override
    public boolean sendEmailCode(String email, String pid, String type) {
        try {
            String code = mockEnabled ? MOCK_CODE : generateCode();
            String key = EMAIL_CODE_PREFIX + email;
            
            // 存储验证码到Redis
            redisTemplate.opsForValue().set(key, code, Duration.ofSeconds(codeExpiration));
            
            if (mockEnabled) {
                log.info("🔧 [MOCK MODE] Email verification code for {}: {}", email, code);
                return true;
            }

            if (fromEmail == null || fromEmail.trim().isEmpty()) {
                log.warn("📧 Email service not configured, using mock mode for email: {}, code: {}", email, code);
                return true;
            }
            
            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("BuddyFindr 验证码");
            message.setText(String.format("验证码：%s，有效期5分钟。", code));
            
            mailSender.send(message);
            
            log.info("📧 Email verification code sent to: {}", email);
            return true;
        } catch (Exception e) {
            log.error("❌ Failed to send email verification code to: {}", email, e);
            
            // 如果邮件发送失败，在开发模式下仍然返回成功
            if (mockEnabled) {
                log.info("🔧 [MOCK MODE] Simulating email sent for: {}", email);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean sendSmsCode(String zone, String phone, String pid, String lang) {
        try {
            String code = mockEnabled ? MOCK_CODE : generateCode();
            String key = SMS_CODE_PREFIX + zone + phone;
            
            // 存储验证码到Redis
            redisTemplate.opsForValue().set(key, code, Duration.ofSeconds(codeExpiration));
            
            if (mockEnabled || !smsEnabled) {
                log.info("🔧 [MOCK MODE] SMS verification code for {}{}: {}", zone, phone, code);
                return true;
            }

            // 发送短信
            DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            IAcsClient client = new DefaultAcsClient(profile);
            
            SendSmsRequest request = new SendSmsRequest();
            request.setPhoneNumbers(phone);
            request.setSignName(signName);
            request.setTemplateCode(templateCode);
            request.setTemplateParam("{\"code\":\"" + code + "\"}");
            
            SendSmsResponse response = client.getAcsResponse(request);
            
            if ("OK".equals(response.getCode())) {
                log.info("📱 SMS verification code sent to: {}{}", zone, phone);
                return true;
            } else {
                log.error("❌ Failed to send SMS: {}", response.getMessage());
                return false;
            }
        } catch (Exception e) {
            log.error("❌ Failed to send SMS verification code to: {}{}", zone, phone, e);
            
            // 如果短信发送失败，在开发模式下仍然返回成功
            if (mockEnabled) {
                log.info("🔧 [MOCK MODE] Simulating SMS sent for: {}{}", zone, phone);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        try {
            String key = EMAIL_CODE_PREFIX + email;
            String storedCode = redisTemplate.opsForValue().get(key);
            
            if (storedCode != null && storedCode.equals(code)) {
                // 验证成功后删除验证码
                redisTemplate.delete(key);
                log.info("✅ Email verification code verified for: {}", email);
                return true;
            }
            
            // 在模拟模式下，接受固定验证码
            if (mockEnabled && MOCK_CODE.equals(code)) {
                log.info("🔧 [MOCK MODE] Email verification code accepted for: {}", email);
                return true;
            }
            
            log.warn("❌ Invalid email verification code for: {}", email);
            return false;
        } catch (Exception e) {
            log.error("❌ Failed to verify email code for: {}", email, e);
            return false;
        }
    }

    @Override
    public boolean verifySmsCode(String phone, String code) {
        try {
            // 这里需要传入完整的phone（包含区号），暂时简化处理
            String key = SMS_CODE_PREFIX + phone;
            String storedCode = redisTemplate.opsForValue().get(key);
            
            if (storedCode != null && storedCode.equals(code)) {
                // 验证成功后删除验证码
                redisTemplate.delete(key);
                log.info("✅ SMS verification code verified for: {}", phone);
                return true;
            }
            
            // 在模拟模式下，接受固定验证码
            if (mockEnabled && MOCK_CODE.equals(code)) {
                log.info("🔧 [MOCK MODE] SMS verification code accepted for: {}", phone);
                return true;
            }
            
            log.warn("❌ Invalid SMS verification code for: {}", phone);
            return false;
        } catch (Exception e) {
            log.error("❌ Failed to verify SMS code for: {}", phone, e);
            return false;
        }
    }

    @Override
    public String generateCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        
        for (int i = 0; i < codeLength; i++) {
            code.append(random.nextInt(10));
        }
        
        return code.toString();
    }

    @Override
    public void clearCode(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("❌ Failed to clear code for key: {}", key, e);
        }
    }
} 