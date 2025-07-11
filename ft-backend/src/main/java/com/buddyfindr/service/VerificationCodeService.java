package com.buddyfindr.service;

public interface VerificationCodeService {
    
    /**
     * Send email verification code
     */
    boolean sendEmailCode(String email, String pid, String type);
    
    /**
     * Send SMS verification code
     */
    boolean sendSmsCode(String zone, String phone, String pid, String lang);
    
    /**
     * Verify email verification code
     */
    boolean verifyEmailCode(String email, String code);
    
    /**
     * Verify SMS verification code
     */
    boolean verifySmsCode(String phone, String code);
    
    /**
     * Generate verification code
     */
    String generateCode();
    
    /**
     * Clear verification code
     */
    void clearCode(String key);
} 