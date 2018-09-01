package com.dajeong.chatbot.dajeongbot.model.request;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class RequestSendMessage {
    /*
    *   paramObject.put("account_id", accountId);
            paramObject.put("content", content);
            paramObject.put("chat_type", chatType);
            paramObject.put("time", time);
            paramObject.put("isBot", isBot);
            paramObject.put("response", mJsonResponse);
    * */

    @SerializedName("account_id")
    private int accountId;
    private String content;
    @SerializedName("node_type")
    private int nodeType;
    @SerializedName("chat_type")
    private int chatType;
    private String time;
    private int isBot;
    private JsonObject response;

    public RequestSendMessage(int accountId, String content, int nodeType, int chatType, String time, int isBot, JsonObject response) {
        this.accountId = accountId;
        this.content = content;
        this.nodeType = nodeType;
        this.chatType = chatType;
        this.time = time;
        this.isBot = isBot;
        this.response = response;
    }

    public int getNodeType() {
        return nodeType;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getContent() {
        return content;
    }

    public int getChatType() {
        return chatType;
    }

    public String getTime() {
        return time;
    }

    public int getIsBot() {
        return isBot;
    }

    public JsonObject getResponse() {
        return response;
    }
}
