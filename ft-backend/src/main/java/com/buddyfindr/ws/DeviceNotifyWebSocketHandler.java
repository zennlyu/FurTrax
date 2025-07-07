package com.buddyfindr.ws;

import com.buddyfindr.dto.DeviceNotifyDto;
import com.buddyfindr.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class DeviceNotifyWebSocketHandler extends TextWebSocketHandler {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 设备ID -> WebSocketSession
    private final Map<String, WebSocketSession> deviceSessionMap = new ConcurrentHashMap<>();
    // 用户ID -> WebSocketSession
    private final Map<Long, WebSocketSession> userSessionMap = new ConcurrentHashMap<>();

    @Value("/ws/notify")
    private String wsPath;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 握手时需带 token 和 cid/userId
        String token = getParam(session, "token");
        String cid = getParam(session, "cid");
        String userIdStr = getParam(session, "userId");
        if (token == null || (cid == null && userIdStr == null)) {
            session.close();
            return;
        }
        jwtUtil.getEmailFromToken(token); // 校验token
        if (cid != null) {
            deviceSessionMap.put(cid, session);
        } else if (userIdStr != null) {
            userSessionMap.put(Long.valueOf(userIdStr), session);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 设备或服务端推送消息到用户
        DeviceNotifyDto dto = objectMapper.readValue(message.getPayload(), DeviceNotifyDto.class);
        // 推送给用户（假设有 userId 关联，实际可根据业务扩展）
        for (WebSocketSession userSession : userSessionMap.values()) {
            if (userSession.isOpen()) {
                userSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(dto)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        deviceSessionMap.values().remove(session);
        userSessionMap.values().remove(session);
    }

    private String getParam(WebSocketSession session, String key) {
        String query = session.getUri().getQuery();
        if (query == null) return null;
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals(key)) return kv[1];
        }
        return null;
    }
} 