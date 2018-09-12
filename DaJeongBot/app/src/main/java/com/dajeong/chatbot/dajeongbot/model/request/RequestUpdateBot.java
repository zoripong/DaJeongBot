package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestUpdateBot {
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("new_bot_type")
    private String newBotType;

    public RequestUpdateBot(int accountId, String newBotType) {
        this.accountId = accountId;
        this.newBotType = newBotType;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getNewBotType() {
        return newBotType;
    }

    @Override
    public String toString() {
        return "RequestUpdateBot{" +
                "accountId=" + accountId +
                ", newBotType='" + newBotType + '\'' +
                '}';
    }
}
