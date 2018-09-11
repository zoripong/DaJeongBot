package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.annotations.SerializedName;

public class RequestUpdateName {
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("new_name")
    private String newName;

    public RequestUpdateName(int accountId, String newName) {
        this.accountId = accountId;
        this.newName = newName;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getNewName() {
        return newName;
    }

    @Override
    public String toString() {
        return "RequestUpdateName{" +
                "accountId=" + accountId +
                ", newName='" + newName + '\'' +
                '}';
    }
}
