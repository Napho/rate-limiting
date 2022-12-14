package com.irembo.interview.controller;

import com.irembo.interview.data.SendSMSResponse;
import com.irembo.interview.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class RateLimitedController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public RateLimitedController(final SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

    @PostMapping(value = "/sendSMS", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendSms() {
        final SendSMSResponse response = subscriptionService.sendSMS("Send same SMS");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity sendEmail() {
        final SendSMSResponse response = subscriptionService.sendSMS("Send same email");
        return ResponseEntity.ok(response);
    }


}
