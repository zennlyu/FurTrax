package com.buddyfindr.interceptor;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CachedBodyHttpServletRequestFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            
            // 只对POST和PUT请求进行包装
            String method = httpRequest.getMethod();
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method)) {
                CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
                chain.doFilter(cachedRequest, response);
                return;
            }
        }
        
        chain.doFilter(request, response);
    }
} 