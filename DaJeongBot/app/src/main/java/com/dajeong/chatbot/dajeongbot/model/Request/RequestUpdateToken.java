package com.dajeong.chatbot.dajeongbot.model.request;

public class RequestUpdateToken {
    private int accountId;
    private String fcmToken;
    private String newToken;

    public RequestUpdateToken(int accountId, String fcmToken, String newToken) {
        this.accountId = accountId;
        this.fcmToken = fcmToken;
        this.newToken = newToken;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public String getNewToken() {
        return newToken;
    }

    @Override
    public String toString() {
        return "RequestUpdateToken{" +
                "accountId=" + accountId +
                ", fcmToken='" + fcmToken + '\'' +
                ", newToken='" + newToken + '\'' +
                '}';
    }
}
