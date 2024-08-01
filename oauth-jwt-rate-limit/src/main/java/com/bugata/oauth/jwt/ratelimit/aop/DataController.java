package com.bugata.oauth.jwt.ratelimit.aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugata.oauth.jwt.ratelimit.config.RateLimit;

@RestController
public class DataController {

    @RateLimit
    @GetMapping("/data")
    public String getData() {
        // Logic to retrieve data
        return "Some data";
    }
}
