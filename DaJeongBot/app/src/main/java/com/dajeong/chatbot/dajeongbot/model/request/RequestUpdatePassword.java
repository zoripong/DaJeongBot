package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestUpdatePassword {
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("new_password")
    private String newPassword;

    public RequestUpdatePassword(int accountId, String newPassword) {
        this.accountId = accountId;
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "RequestUpdatePassword{" +
                "accountId=" + accountId +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
