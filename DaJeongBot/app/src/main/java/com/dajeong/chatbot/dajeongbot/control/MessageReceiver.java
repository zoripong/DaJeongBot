package com.dajeong.chatbot.dajeongbot.control;

import android.util.Log;

import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.Memory;
import com.dajeong.chatbot.dajeongbot.network.RetrofitService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Mirim on 2018-09-10.
 */

public class MessageReceiver {
    private final String TAG = "MessageReceiver";

    private static MessageReceiver instance;
    public static MessageReceiver getInstance() {
        if( instance == null){
            instance = new MessageReceiver();
        }
        return instance;
    }

    private MessageReceiver(){}

    public void receiveCarouselMessage(JsonObject result, LinkedList<Chat> mChats, Character mBotChar){
        if(result.has("events")){
            // carousel item
            Log.e(TAG, "추억회상");
            // TODO : 버튼 클릭시 데이터 요청
            String timestamp = String.valueOf(result.get("time").getAsLong());
            JsonArray messages = result.getAsJsonArray("content");
            JsonArray events = result.getAsJsonArray("events");

            for(int i = 0; i<messages.size(); i++){
                Log.e(TAG, "messages"+i);
                if(i < messages.size()-1){
                    mChats.addLast(new Chat(NodeType.SPEAK_NODE, mBotChar, messages.get(i).getAsString(), timestamp));
                }else{
                    if(events.size() > 0){
                        ArrayList<Memory> memories = new ArrayList<>();
                        for(int j = 0; j<events.size(); j++){
                            Log.e(TAG, "event"+j);
                            JsonObject event = events.get(j).getAsJsonObject();
                            memories.add(new Memory(event.get("id").getAsInt(), event.get("event_image").getAsString(),  event.get("event_detail").getAsString()));
                        }
                        memories.add(new Memory(-1, "", "이제 없어!"));
                        mChats.addLast(new Chat(NodeType.CAROUSEL_NODE, mBotChar, messages.get(i).getAsString(), timestamp, memories));
                    }else{
                        mChats.addLast(new Chat(NodeType.SPEAK_NODE, mBotChar, messages.get(i).getAsString(), result.get("time").getAsString()));
                    }
                }
            }
        }
    }
    public void receiveBasicMessage(JsonObject result, LinkedList<Chat> mChats, Character mBotChar){
        JsonArray messages = result.getAsJsonArray("content");
        messages = result.get("content").getAsJsonArray();

        for(int i = 0; i<messages.size(); i++){
            mChats.addLast(new Chat(result.get("node_type").getAsInt(), mBotChar, messages.get(i).getAsString(), result.get("time").getAsString()));
        }
    }

    public void receiveDanbeeMessage(JsonObject mJsonResponse, LinkedList<Chat>mChats, Character mBotChar){
        JsonArray jsonArray = mJsonResponse.getAsJsonArray("result");
        for( int i = 0; i<jsonArray.size(); i++ ){
            String imgUrl = jsonArray.get(i).getAsJsonObject().get("imgRoute").getAsString();
            // TODO : 이미지 노드 추가하기
            String message = jsonArray.get(i).getAsJsonObject().get("message").getAsString();
            String timestamp = String.valueOf(jsonArray.get(i).getAsJsonObject().get("timestamp").getAsLong());
            String nodeType = jsonArray.get(i).getAsJsonObject().get("nodeType").getAsString();
            Log.e(TAG, "node type is " + nodeType);
            JsonArray options = jsonArray.get(i).getAsJsonObject().getAsJsonArray("optionList");

            if(options.size() > 0){
                //TODO : EditText enable
                mChats.addLast(new Chat(NodeType.SLOT_NODE, mBotChar, message, timestamp, options));
            }else{
                mChats.addLast(new Chat(NodeType.SPEAK_NODE, mBotChar, message, timestamp));
            }
        }
    }



}



