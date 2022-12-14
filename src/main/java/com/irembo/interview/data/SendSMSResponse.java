package com.irembo.interview.data;

public record SendSMSResponse(Integer statusCode, String description) {

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getDescription() {
        return description;
    }
}
