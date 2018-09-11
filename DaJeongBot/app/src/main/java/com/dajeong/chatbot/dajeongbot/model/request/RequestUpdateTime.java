package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestUpdateTime {
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("new_notify_time")
    private String newNotifyTime;
    @SerializedName("new_ask_time")
    private String newAskTime;

    public RequestUpdateTime(int accountId, String newNotifyTime, String newAskTime) {
        this.accountId = accountId;
        this.newNotifyTime = newNotifyTime;
        this.newAskTime = newAskTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getNewNotifyTime() {
        return newNotifyTime;
    }

    public String getNewAskTime() {
        return newAskTime;
    }

    @Override
    public String toString() {
        return "RequestUpdateTime{" +
                "accountId=" + accountId +
                ", newNotifyTime='" + newNotifyTime + '\'' +
                ", newAskTime='" + newAskTime + '\'' +
                '}';
    }
}
