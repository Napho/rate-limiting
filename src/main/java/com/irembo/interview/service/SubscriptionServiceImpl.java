package com.irembo.interview.service;

import com.irembo.interview.data.SendSMSResponse;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    @Override
    public SendSMSResponse sendSMS(String payload) {
        //Random things here
        return new SendSMSResponse(00,"Successfully Sent");
    }
}
