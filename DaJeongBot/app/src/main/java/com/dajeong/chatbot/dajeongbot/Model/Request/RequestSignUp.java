package com.dajeong.chatbot.dajeongbot.Model.Request;

import com.google.gson.annotations.SerializedName;

public class RequestSignUp {

    @SerializedName("user_id")
    private String userId;
    private String name, birthday;
    @SerializedName("bot_type")
    private int botType;
    @SerializedName("account_type")
    private int accountType;
    private String password, token;

    public RequestSignUp(String userId, String name, String birthday, int botType, int accountType, String password, String token) {
        this.userId = userId;
        this.name = name;
        this.birthday = birthday;
        this.botType = botType;
        this.accountType = accountType;
        this.password = password;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getBotType() {
        return botType;
    }

    public int getAccountType() {
        return accountType;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }
}
