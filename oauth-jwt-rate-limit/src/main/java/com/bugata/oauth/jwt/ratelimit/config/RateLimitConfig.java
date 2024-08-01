package com.bugata.oauth.jwt.ratelimit.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bugata.oauth.jwt.ratelimit.aop.RateLimitAspect;

@Configuration
@EnableWebMvc
public class RateLimitConfig {

    @Bean
    public RateLimitAspect rateLimitAspect() {
        return new RateLimitAspect();
    }

}
