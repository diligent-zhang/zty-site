package com.zty.ztysite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 是一种单向加密算法，用于安全存储密码
        // 注意：这里只引入了 spring-security-crypto，不会激活 Spring Security 的认证拦截
        return new BCryptPasswordEncoder();
    }
}