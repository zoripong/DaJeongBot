package com.dajeong.chatbot.dajeongbot.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.Model.Chat;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.Vector;

public class ChatAdapter extends RecyclerView.Adapter {

    private Vector<Chat> mChats;
    private Context mContext;

    public ChatAdapter(Vector<Chat> chats, Context context){
        this.mChats = chats;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatHolder(v);
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        ChatHolder chatHolder = (ChatHolder)holder;
        Chat chat = mChats.get(position);
        if(chat.getSender() != null){
            // 사용자
            chatHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
            chatHolder.mTvSenderName.setText(chat.getSender().getName());
        }
        chatHolder.mTvContent.setText(chat.getContent());
        chatHolder.mTvTime.setText(chat.getTime().toString());

    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private class ChatHolder extends RecyclerView.ViewHolder{
        ImageView mIvSenderProfile;
        TextView mTvSenderName;
        TextView mTvContent;
        TextView mTvTime;

        public ChatHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mTvSenderName = itemView.findViewById(R.id.tvSenderName);
            mTvContent = itemView.findViewById(R.id.tvContent);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
