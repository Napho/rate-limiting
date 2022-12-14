package com.irembo.interview.controller;

import com.irembo.interview.service.RateLimiter;
import com.irembo.interview.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/management")
public class GenericController {

    private final RateLimiter rateLimiter;

    @Autowired
    public GenericController(final RateLimiter rateLimiter){
        this.rateLimiter = rateLimiter;
    }

    @PostMapping("/tps/buy")
    public ResponseEntity buyTPS(@PathVariable("amount") Long amount, @RequestHeader("clientId") String clientId) {
        rateLimiter.buyTPS(clientId,amount);
        return ResponseEntity.ok().build();
    }
}
