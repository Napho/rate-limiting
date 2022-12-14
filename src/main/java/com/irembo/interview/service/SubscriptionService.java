package com.irembo.interview.service;

import com.irembo.interview.data.SendSMSResponse;

public interface SubscriptionService {

    SendSMSResponse sendSMS(String payload);
}
