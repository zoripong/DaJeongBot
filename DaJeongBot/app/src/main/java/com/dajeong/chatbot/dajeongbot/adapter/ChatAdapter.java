package com.dajeong.chatbot.dajeongbot.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                chatBotHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 1:
                ChatUserHolder chatUserHolder = (ChatUserHolder)holder;
                chatUserHolder.mTvContent.setText(chat.getContent());
                chatUserHolder.mTvTime.setText(new SimpleDateFormat( "a HH:mm", Locale.KOREA ).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 2:
                ChatBotSlotHolder chatBotSlotHolder = (ChatBotSlotHolder) holder;
                chatBotSlotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotSlotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                if(!chatBotSlotHolder.hasBtn){
                    chatBotSlotHolder.setHasBtn(true);
                    createSlotBtns(chatBotSlotHolder.mRootLayout, chat.getOptionList());
                }

                chatBotSlotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            default:
                return;
        }


        }

    @Override
    public int getItemViewType(int position) {
        if(mChats.get(position).getSender()!=null){
            // 챗봇이 전송
            if(mChats.get(position).getNodeType()== NodeType.SLOT_NODE
                    && mChats.get(position).getOptionList() != null){
                // slot node 일 경우
                return 2;
            }
            if(mChats.get(position).getNodeType() == NodeType.CAROUSEL_NODE
                    && mChats.get(position).getCarouselList() != null){
                // carousel node 일 경우
                return 3;
            }
            return 0;
        } else {
            // 사용자가 전송
            return 1;
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private void createSlotBtns(LinearLayout layout, JsonArray options) {

        for (int i = 0; i < options.size(); i++) {
            //{'value': 'yes', 'id': '1', 'type': 'btn', 'label': '응'}
            final JsonObject option = options.get(i).getAsJsonObject();
            Button myButton = new Button(mContext);
            myButton.setText(option.get("label").getAsString());
            // 가로 세로 지정
            myButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            // 스타일 지정
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(android.R.attr.borderlessButtonStyle, typedValue, true);
            if (typedValue.resourceId != 0) {
//                myButton.setBackgroundResource(typedValue.resourceId);
            } else {
//                myButton.setBackgroundColor(typedValue.data);
            }

            // 배경 지정
            final int version = Build.VERSION.SDK_INT;
            if (version >= 21) {
                myButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chatbot_tv_slot_custom));
            } else {
                myButton.setBackground(mContext.getResources().getDrawable(R.drawable.chatbot_tv_slot_custom));
            }

            // 마진 지정
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) myButton.getLayoutParams();
//            int dp = (int)convertPixelsToDp(5.0f);
//            p.setMargins(dp, dp, dp, dp);
//            myButton.requestLayout();


            myButton.setId(i);
            final int id_ = myButton.getId();

//            LinearLayout layout = (LinearLayout) findViewById(R.id.myDynamicLayout);
            layout.addView(myButton);

            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    int accountId = Integer.parseInt(CustomSharedPreference.getInstance(mContext, "user_info").getStringPreferences("id"));
                    ((MainActivity)mContext).sendMessage(accountId, option.get("value").getAsString(), ChatType.REGISTER_CHAT, String.valueOf(System.currentTimeMillis()), 0);
                }
            });
        }
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
        LinearLayout mRootLayout;
        ImageView mIvSenderProfile;
        TextView mTvTime;
        boolean hasBtn;

        public ChatBotSlotHolder(View itemView){
            super(itemView);
            mRootLayout = itemView.findViewById(R.id.ll_dynamic_btns);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            hasBtn = false;
            mTvTime = itemView.findViewById(R.id.tvTime);
        }

        public void setHasBtn(boolean hasBtn) {
            this.hasBtn = hasBtn;
        }
    }

}
