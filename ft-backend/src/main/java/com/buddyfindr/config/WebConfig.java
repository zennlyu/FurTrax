package com.buddyfindr.config;

import com.buddyfindr.interceptor.CachedBodyHttpServletRequestFilter;
import com.buddyfindr.interceptor.SignatureInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SignatureInterceptor signatureInterceptor;
    private final CachedBodyHttpServletRequestFilter cachedBodyFilter;

    @Bean
    public FilterRegistrationBean<CachedBodyHttpServletRequestFilter> cachedBodyFilterRegistration() {
        FilterRegistrationBean<CachedBodyHttpServletRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(cachedBodyFilter);
        registrationBean.addUrlPatterns("/v1/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signatureInterceptor)
                .addPathPatterns("/v1/**")
                .excludePathPatterns(
                        "/v1/check_email",
                        "/v1/check_user", 
                        "/v1/register",
                        "/v1/login",
                        "/v1/login/vcode",
                        "/v1/sms/login",
                        "/v1/third_login",
                        "/v1/refresh",
                        "/v1/vcode/login",
                        "/v1/vcode/reset_user_pwd",
                        "/v1/reset_user_pwd",
                        "/v1/sms/send",
                        "/v1/zones",
                        "/v1/upload/**"
                );
    }
} 