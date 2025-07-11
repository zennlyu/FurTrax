package com.buddyfindr.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StandardApiResponse<T> {
    private Integer code;
    private String reason;
    private String message;
    private Map<String, Object> metadata;
    private T data;

    // Success response
    public static <T> StandardApiResponse<T> success(T data) {
        StandardApiResponse<T> response = new StandardApiResponse<>();
        response.setData(data);
        return response;
    }

    // Error response
    public static <T> StandardApiResponse<T> error(Integer code, String reason, String message) {
        StandardApiResponse<T> response = new StandardApiResponse<>();
        response.setCode(code);
        response.setReason(reason);
        response.setMessage(message);
        return response;
    }

    // Error response (with metadata)
    public static <T> StandardApiResponse<T> error(Integer code, String reason, String message, Map<String, Object> metadata) {
        StandardApiResponse<T> response = new StandardApiResponse<>();
        response.setCode(code);
        response.setReason(reason);
        response.setMessage(message);
        response.setMetadata(metadata);
        return response;
    }
} 