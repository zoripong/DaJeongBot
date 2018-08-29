package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestRegisterToken {
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("fcm_token")
    private String fcmToken;

    public RequestRegisterToken(int accountId, String fcmToken) {
        this.accountId = accountId;
        this.fcmToken = fcmToken;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    @Override
    public String toString() {
        return "RequestRegisterToken{" +
                "accountId=" + accountId +
                ", fcmToken=" + fcmToken +
                '}';
    }
}
