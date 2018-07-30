package com.dajeong.chatbot.dajeongbot.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.Model.Chat;
import com.dajeong.chatbot.dajeongbot.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "ChatAdapter";
    private Vector<Chat> mChats;
    private Context mContext;
    View v;
    public ChatAdapter(Vector<Chat> chats, Context context){
        this.mChats = chats;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot, parent, false);
         switch(viewType){
             case 0: return new ChatBotHolder(v);
             case 1: return new ChatUserHolder(v);
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
                chatBotHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(chat.getTime()));
                break;
            case 1:
                ChatUserHolder chatUserHolder = (ChatUserHolder)holder;
                chatUserHolder.mTvContent.setText(chat.getContent());
                chatUserHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(chat.getTime()));
                break;
            default:
                return;
        }


        }

    @Override
    public int getItemViewType(int position) {
        if(mChats.get(position).getSender()!=null){
            return 0;
        }else{
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
            mTvContent = itemView.findViewById(R.id.tvContent);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
