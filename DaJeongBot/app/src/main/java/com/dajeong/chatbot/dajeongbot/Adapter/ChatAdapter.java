package com.dajeong.chatbot.dajeongbot.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.Model.Chat;
import com.dajeong.chatbot.dajeongbot.R;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

public class ChatAdapter extends RecyclerView.Adapter {
    private final String TAG = "ChatAdapter";
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
            // 챗봇
            //Log.e(TAG, "bot: "+position);
            //chatHolder.mTvSenderName.setVisibility(View.VISIBLE);
            chatHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
            chatHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
            //chatHolder.mTvSenderName.setText(chat.getSender().getName());
        }else{
            //Log.e(TAG, "user: "+position);
            chatHolder.mIvSenderProfile.setVisibility(View.GONE);
            //chatHolder.mTvSenderName.setVisibility(View.GONE);
            //나중에 시간을 서버에서 가져와서 분이 똑같으면 프로필 이미지랑 시간을 안보여주는게 레이아웃이 훨씬 깔끔할듯
            chatHolder.mTvContent.setBackgroundResource(R.drawable.chatbot_tv_me_custom);

            //오른쪽 정렬 왜 안될까,,
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.connect(R.id.tvContent,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,0);
            constraintSet.connect(R.id.tvTime,ConstraintSet.END,R.id.tvContent, ConstraintSet.START,0);
        }
        chatHolder.mTvContent.setText(chat.getContent());

        chatHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(chat.getTime()));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private class ChatHolder extends RecyclerView.ViewHolder{
        ImageView mIvSenderProfile;
        //TextView mTvSenderName;
        TextView mTvContent;
        TextView mTvTime;

        public ChatHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            //mTvSenderName = itemView.findViewById(R.id.tvSenderName);
            mTvContent = itemView.findViewById(R.id.tvContent);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
