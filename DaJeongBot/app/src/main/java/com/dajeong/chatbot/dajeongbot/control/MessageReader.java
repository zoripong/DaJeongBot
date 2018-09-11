package com.dajeong.chatbot.dajeongbot.control;

import android.util.Log;

import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.Memory;
import com.dajeong.chatbot.dajeongbot.model.Slot;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Mirim on 2018-09-11.
 */

public class MessageReader {
    private final String TAG = "MessageReader";

    private static MessageReader instance;
    public static MessageReader getInstance() {
        if( instance == null){
            instance = new MessageReader();
        }
        return instance;
    }

    private MessageReader(){}

    public void readBasicMessage(JsonObject json, LinkedList<Chat> mChats, Character mBotChar){
        // carousel_list 와 slot list 가 비어있을 경우
        // 챗봇인지 아닌지 확인하기
        if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 0){
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), null, json.get("content").getAsString(), json.get("time").getAsString()));
        }
        else if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 1) {
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), mBotChar, json.get("content").getAsString(), json.get("time").getAsString()));
        }
    }

    public void readCarouselMessage(JsonObject json, LinkedList<Chat> mChats, Character mBotChar){
        Log.e(TAG, "carousel_list가 있습니다.");
        // carousel_list 가 있을 경우
//                Log.e(TAG, json.toString());
        JSONArray carouselList = null;
        ArrayList<Memory> memories = new ArrayList<>();
        try {
            carouselList = new JSONArray(json.get("carousel_list").getAsString());
            for(int j = 0; j<carouselList.length(); j++){
                memories.add(new Memory(carouselList.getJSONObject(j).getInt("id"), null,
                        carouselList.getJSONObject(j).getString("schedule_where")+"에서"+carouselList.getJSONObject(j).getString("schedule_what")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "carousel_list를 파싱하는데 문제가 생겼습니다..");

        }
        Log.e(TAG, "carousel_list의 길이는 : "+memories.size());

        if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 0){
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), null, json.get("content").getAsString(), json.get("time").getAsString(), null, memories));
        }
        else if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 1) {
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), mBotChar, json.get("content").getAsString(), json.get("time").getAsString(), null, memories));
        }
    }

    public void readSlotMessage(JsonObject json, LinkedList<Chat> mChats, Character mBotChar) {
        Log.e(TAG, "slot_list가 있습니다.");
        JSONArray slotList = null;
        ArrayList<Slot> slots = new ArrayList<>();
        try {
//            Log.e(TAG, "slot_list : "+json.get("slot_list").getAsString());
            slotList = new JSONArray(json.get("slot_list").getAsString());
            for(int j = 0; j<slotList.length(); j++){
                slots.add(new Slot(slotList.getJSONObject(j).getInt("id"),
                        slotList.getJSONObject(j).getString("schedule_where")+"에서 "+slotList.getJSONObject(j).getString("schedule_what"),
                        slotList.getJSONObject(j).getString("id")));
            }
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e(TAG, "slot_list 를 파싱하는데 문제가 생겼습니다..\n"+e.toString());

        }
        Log.e(TAG, "slot_list 의 길이는 : "+slots.size());

        if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 0){
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), null, json.get("content").getAsString(), json.get("time").getAsString(), slots,null));
        }
        else if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 1) {
            mChats.addFirst(new Chat(json.get("node_type").getAsInt(), mBotChar, json.get("content").getAsString(), json.get("time").getAsString(), slots,null));
        }

    }

}
