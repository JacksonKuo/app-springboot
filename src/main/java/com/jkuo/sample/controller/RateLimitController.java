package com.jkuo.sample.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jkuo.sample.service.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RateLimitController {

    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimiterService) {
        this.rateLimitService = rateLimiterService;
    }

    @GetMapping("/ratelimit")
    public String ratelimit(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        boolean permitAcquired = rateLimitService.getRateLimiter(clientIp).tryAcquire(1);
        if (!permitAcquired) {
            return "Rate limit exceeded: " + clientIp;
        }
        return "Successful";
    }
}