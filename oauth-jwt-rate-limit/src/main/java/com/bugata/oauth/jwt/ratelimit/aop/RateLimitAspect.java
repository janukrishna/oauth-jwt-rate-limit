package com.bugata.oauth.jwt.ratelimit.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.bugata.oauth.jwt.services.JwtService;

@Aspect
@Component
public class RateLimitAspect {

    private final Map<String, Instant> requestTimestamps = new ConcurrentHashMap<>();
    private final Map<String, Integer> requestCounts = new ConcurrentHashMap<>();

    @Value("${ratelimit.window.seconds}")
    private int rateLimitWindowSeconds;

    @Value("${ratelimit.max.requests}")
    private int rateLimitMaxRequests;
    
    @Autowired
    private JwtService jwtService;

    @Around("@annotation(com.bugata.oauth.jwt.ratelimit.config.RateLimit)")
    public Object rateLimitCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();

        String clientId = request.getHeader("X-Client-Id");
        String endpoint = request.getRequestURI();

        String key = clientId + ":" + endpoint;
        
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            System.out.println(token);
            username = jwtService.extractUsername(token);
            System.out.println(username);
        }
        
        key = username;
        System.out.println(key);
        Instant now = Instant.now();

        // Increment request count for the client and endpoint
        requestCounts.putIfAbsent(key, 0);
        requestCounts.computeIfPresent(key, (k, v) -> {
            Instant lastRequestTime = requestTimestamps.getOrDefault(k, Instant.EPOCH);
            if (lastRequestTime.isBefore(now.minusSeconds(rateLimitWindowSeconds))) {
                requestTimestamps.put(k, now);
                return 1; // Reset count after window passes
            } else {
                return v + 1;
            }
        });

        int requestCount = requestCounts.get(key);

        // Check if request count exceeds rate limit
        if (requestCount > rateLimitMaxRequests) {
            setRateLimitHeaders(response, 0, calculateResetTime(now));
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Try again later.");
            return null;
        }

        // Update request timestamp for the client and endpoint
        requestTimestamps.put(key, now);
        setRateLimitHeaders(response, rateLimitMaxRequests - requestCount, calculateResetTime(now));

        return joinPoint.proceed(); // Proceed with the method execution
    }

    private void setRateLimitHeaders(HttpServletResponse response, int remaining, long resetTime) {
        response.setHeader("RateLimit-Limit", String.valueOf(rateLimitMaxRequests));
        response.setHeader("RateLimit-Remaining", String.valueOf(remaining));
        response.setHeader("RateLimit-Reset", String.valueOf(resetTime));
        response.setHeader("Retry-After", String.valueOf(resetTime));
    }

    private long calculateResetTime(Instant now) {
        Instant nextResetTime = now.plusSeconds(rateLimitWindowSeconds);
        return Duration.between(now, nextResetTime).getSeconds();
    }
}
