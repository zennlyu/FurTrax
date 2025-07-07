package com.buddyfindr.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class SignatureInterceptor implements HandlerInterceptor {

    @Value("${signature.secret-key}")
    private String secretKey;
    
    @Value("${signature.enabled:true}")
    private boolean signatureEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果签名验证被禁用（本地开发模式），直接通过
        if (!signatureEnabled) {
            log.debug("🔧 [DEV MODE] Signature verification disabled");
            return true;
        }
        
        String method = request.getMethod();
        
        // 只对POST和PUT请求进行签名验证
        if (!"POST".equalsIgnoreCase(method) && !"PUT".equalsIgnoreCase(method)) {
            return true;
        }
        
        // 跳过某些不需要签名的端点
        String requestURI = request.getRequestURI();
        if (isExcludedPath(requestURI)) {
            return true;
        }
        
        String fcSign = request.getHeader("FC-Sign");
        if (fcSign == null || fcSign.trim().isEmpty()) {
            sendErrorResponse(response, 400, "BAD_REQUEST", "FC-Sign header is missing");
            return false;
        }
        
        try {
            String body = getRequestBody(request);
            String expectedSign = generateSignature(body);
            
            if (!fcSign.equals(expectedSign)) {
                log.warn("Invalid signature. Expected: {}, Got: {}", expectedSign, fcSign);
                sendErrorResponse(response, 401, "UNAUTHORIZED", "Invalid signature");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            log.error("Error verifying signature", e);
            sendErrorResponse(response, 500, "INTERNAL_ERROR", "Error verifying signature");
            return false;
        }
    }
    
    private boolean isExcludedPath(String path) {
        // 排除文件上传和某些公开端点
        return path.startsWith("/v1/upload/") || 
               path.equals("/v1/check_email") ||
               path.equals("/v1/check_user") ||
               path.equals("/v1/register") ||
               path.equals("/v1/login") ||
               path.equals("/v1/login/vcode") ||
               path.equals("/v1/sms/login") ||
               path.equals("/v1/third_login") ||
               path.equals("/v1/refresh") ||
               path.equals("/v1/vcode/login") ||
               path.equals("/v1/vcode/reset_user_pwd") ||
               path.equals("/v1/reset_user_pwd") ||
               path.equals("/v1/sms/send");
    }
    
    private String getRequestBody(HttpServletRequest request) throws IOException {
        if (request instanceof CachedBodyHttpServletRequest) {
            return ((CachedBodyHttpServletRequest) request).getBody();
        }
        
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }
    
    private String generateSignature(String body) {
        return HmacUtils.hmacMd5Hex(secretKey, body);
    }
    
    private void sendErrorResponse(HttpServletResponse response, int statusCode, String reason, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String errorResponse = String.format(
            "{\"code\":%d,\"reason\":\"%s\",\"message\":\"%s\",\"metadata\":{}}",
            statusCode, reason, message
        );
        
        response.getWriter().write(errorResponse);
    }
} 