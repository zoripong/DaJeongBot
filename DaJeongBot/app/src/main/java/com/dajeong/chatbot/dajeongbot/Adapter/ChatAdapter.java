package com.dajeong.chatbot.dajeongbot.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "ChatAdapter";
    private LinkedList<Chat> mChats;
    private Context mContext;
    View vBot;
    View vUser;
    View vSlot;
    public ChatAdapter(LinkedList<Chat> chats, Context context){
        this.mChats = chats;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vBot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_default, parent, false);
        vUser = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_default, parent, false);
        vSlot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_slot, parent, false);
         switch(viewType){
             case 0: return new ChatBotHolder(vBot);
             case 1: return new ChatUserHolder(vUser);
             case 2: return new ChatBotSlotHolder(vSlot);
            default: return null;
         }
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);
        switch (holder.getItemViewType()){
            case 0:
                ChatBotHolder chatBotHolder = (ChatBotHolder)holder;
                chatBotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotHolder.mTvContent.setText(chat.getContent());
//                chatBotHolder.mTvTime.setText(chat.getTime());
                chatBotHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 1:
                ChatUserHolder chatUserHolder = (ChatUserHolder)holder;
                chatUserHolder.mTvContent.setText(chat.getContent());
//                chatUserHolder.mTvTime.setText(chat.getTime());
                chatUserHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 2:
                ChatBotSlotHolder chatBotSlotHolder = (ChatBotSlotHolder) holder;
                chatBotSlotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotSlotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotSlotHolder.mBtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                chatBotSlotHolder.mBtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
//                chatBotSlotHolder.mTvTime.setText(chat.getTime());
                chatBotSlotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            default:
                return;
        }


        }

    @Override
    public int getItemViewType(int position) {
        if(mChats.get(position).getSender()!=null){
            return 0;
        } else if(mChats.get(position).getChatType() == 1){
          return 2;
        } else{
            return 1;
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private class ChatBotHolder extends RecyclerView.ViewHolder{
        ImageView mIvSenderProfile;
        //TextView mTvSenderName;
        TextView mTvContent;
        TextView mTvTime;

        public ChatBotHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            //mTvSenderName = itemView.findViewById(R.id.tvSenderName);
            mTvContent = itemView.findViewById(R.id.tvContent);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    private class ChatUserHolder extends RecyclerView.ViewHolder{
        TextView mTvContent;
        TextView mTvTime;

        public ChatUserHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tvContentUser);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
        }
    }
    private class ChatBotSlotHolder extends RecyclerView.ViewHolder{
        ImageView mIvSenderProfile;
        Button mBtYes, mBtNo;
        TextView mTvTime;
        public ChatBotSlotHolder(View itemView){
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mBtYes = itemView.findViewById(R.id.btYes);
            mBtNo = itemView.findViewById(R.id.btNo);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }

    }
}
