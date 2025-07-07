package com.buddyfindr.service;

public interface VerificationCodeService {
    
    /**
     * 发送邮箱验证码
     */
    boolean sendEmailCode(String email, String pid, String type);
    
    /**
     * 发送短信验证码
     */
    boolean sendSmsCode(String zone, String phone, String pid, String lang);
    
    /**
     * 验证邮箱验证码
     */
    boolean verifyEmailCode(String email, String code);
    
    /**
     * 验证短信验证码
     */
    boolean verifySmsCode(String phone, String code);
    
    /**
     * 生成验证码
     */
    String generateCode();
    
    /**
     * 清除验证码
     */
    void clearCode(String key);
} 