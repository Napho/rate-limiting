package com.irembo.interview.controller;

import com.irembo.interview.service.RateLimiter;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimitInterceptor  implements HandlerInterceptor {

    private final  RateLimiter ratingService;

    @Autowired
    RateLimitInterceptor(RateLimiter ratingService){
        this.ratingService = ratingService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String clientId = request.getHeader("clientId");
        if (Strings.isEmpty(clientId)) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: clientId");
            return false;
        }

        Bucket tokenBucket = ratingService.resolveBucket(clientId);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            return true;
        } else {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "Too many requests");
            return false;
        }
    }
}