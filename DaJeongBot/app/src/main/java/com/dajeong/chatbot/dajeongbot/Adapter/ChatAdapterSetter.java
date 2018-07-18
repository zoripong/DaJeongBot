package com.dajeong.chatbot.dajeongbot.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.dajeong.chatbot.dajeongbot.Model.Chat;

import java.util.Vector;

public class ChatAdapterSetter {
    ChatAdapter mAdapter;

    Context mContext;
    Activity mNowActivity;

    public ChatAdapterSetter(Context mContext, Activity mNowActivity) {
        this.mContext = mContext;
        this.mNowActivity = mNowActivity;
    }

    public boolean setRecyclerChatView(RecyclerView recyclerView, Vector<Chat> vector){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ChatAdapter(vector, mContext);
        recyclerView.setAdapter(mAdapter);
        return true;
    }
}

