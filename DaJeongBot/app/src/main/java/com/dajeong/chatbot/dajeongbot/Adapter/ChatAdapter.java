package com.dajeong.chatbot.dajeongbot.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.fragment.CarouselFragment;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.model.Memory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "ChatAdapter";
    private LinkedList<Chat> mChats;
    private Context mContext;
    private FragmentManager mFragmentManager;
    //    HashMap<Integer, Integer> mViewPagerState = new HashMap<>();
    private View vBot;
    private View vUser;
    private View vSlot;
    private View vCarousel;
    private int count;

    public ChatAdapter(FragmentManager fragmentManager, LinkedList<Chat> chats, Context context) {
        this.mFragmentManager = fragmentManager;
        this.mChats = chats;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        vBot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_default, parent, false);
        vUser = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_default, parent, false);
        vSlot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_slot, parent, false);
        vCarousel = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_carousel, parent, false);
        switch (viewType) {
            case 0:
                return new ChatBotHolder(vBot);
            case 1:
                return new ChatUserHolder(vUser);
            case 2:
                return new ChatBotSlotHolder(vSlot);
            case 3:
                return new ChatBotCarouselHolder(vCarousel);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Chat chat = mChats.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ChatBotHolder chatBotHolder = (ChatBotHolder) holder;
                chatBotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotHolder.mTvContent.setText(chat.getContent());
//                chatBotHolder.mTvTime.setText(chat.getTime());
                chatBotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 1:
                ChatUserHolder chatUserHolder = (ChatUserHolder) holder;
                chatUserHolder.mTvContent.setText(chat.getContent());
//                chatUserHolder.mTvTime.setText(chat.getTime());
                chatUserHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 2:
                ChatBotSlotHolder chatBotSlotHolder = (ChatBotSlotHolder) holder;
                chatBotSlotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotSlotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotSlotHolder.mBtYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show();
                    }
                });
                chatBotSlotHolder.mBtNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show();
                    }
                });
//                chatBotSlotHolder.mTvTime.setText(chat.getTime());
                chatBotSlotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case 3:
                // TODO: setText 로 i번째 일정 text 넣기, Button 에 일정넣기

                final ChatBotCarouselHolder chatBotCarouselHolder = (ChatBotCarouselHolder) holder;
                chatBotCarouselHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotCarouselHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
//                chatBotCarouselHolder.mTvTime.setText(chat.getTime());
                chatBotCarouselHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));


                // FIXME Test Code

                ArrayList<Memory> memories = new ArrayList<>();
                memories.add(new Memory("이미지 입니다.. ", "일정이지요..1"));
                memories.add(new Memory("이미지 입니다.. ", "일정이지요..2"));
                memories.add(new Memory("이미지 입니다.. ", "일정이지요..3"));
                memories.add(new Memory("이미지 입니다.. ", "일정이지요..4"));
                memories.add(new Memory("이미지 입니다.. ", "일정이지요..5"));

                // end of Test Code

                //TODO: TextView 현재 인덱스 값으로 set, Button에 넣기

                CarouselPagerAdapter carouselPagerAdapter = new CarouselPagerAdapter(mFragmentManager, memories);
                chatBotCarouselHolder.mVpimage.setAdapter(carouselPagerAdapter); // viewpager 에 adapter 달아주기
                for (int i = 0; i < memories.size(); i++) {
                    if (memories.get(i).equals(chatBotCarouselHolder.mVpimage.getCurrentItem() + i)) {
                        chatBotCarouselHolder.mTvSchedule.setText(memories.indexOf(memories.get(i)) + "번째 일정");

                    }
                }

                chatBotCarouselHolder.mBtText.setText("dd");

                break;

            default:
                return;
        }


    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ChatBotCarouselHolder chatBotCarouselHolder = (ChatBotCarouselHolder) holder;
//        mViewPagerState.put(holder.getAdapterPosition(), chatBotCarouselHolder.mVpimage.getCurrentItem());
    }

    @Override
    public int getItemViewType(int position) {
        if (mChats.get(position).getSender() != null) {
            if (mChats.get(position).getNodeType() == NodeType.SLOT_NODE) {
                return 2;
            } else if (mChats.get(position).getNodeType() == NodeType.CAROUSEL_NODE) {
                return 3;
            }
            return 0;
        } else {
            return 1;
        }


    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    private class ChatBotHolder extends RecyclerView.ViewHolder {
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

    private class ChatUserHolder extends RecyclerView.ViewHolder {
        TextView mTvContent;
        TextView mTvTime;

        public ChatUserHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tvContentUser);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
        }
    }

    private class ChatBotSlotHolder extends RecyclerView.ViewHolder {
        ImageView mIvSenderProfile;
        Button mBtYes, mBtNo;
        TextView mTvTime;

        public ChatBotSlotHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mBtYes = itemView.findViewById(R.id.btYes);
            mBtNo = itemView.findViewById(R.id.btNo);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }

    }

    private class ChatBotCarouselHolder extends RecyclerView.ViewHolder {
        ImageView mIvSenderProfile;
        TextView mTvSchedule;
        Button mBtText;
        ViewPager mVpimage;
        TextView mTvTime;
        Button mBtNext, mBtPrevious;

        public ChatBotCarouselHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mTvSchedule = itemView.findViewById(R.id.tvSchedule);
            mBtText = itemView.findViewById(R.id.btText);
            mVpimage = itemView.findViewById(R.id.vp);
            mBtNext = itemView.findViewById(R.id.btn_carousel_next);
            mBtPrevious = itemView.findViewById(R.id.btn_carousel_previous);
            mTvTime = itemView.findViewById(R.id.tvTime);

            mVpimage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }
    }

    public class CarouselPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Memory> memories;

        public CarouselPagerAdapter(FragmentManager fm, ArrayList<Memory> memories) {
            super(fm);
            this.memories = memories;
        }

        @Override
        public Fragment getItem(final int position) {
            return CarouselFragment.newInstance(memories.get(position));
        }

        @Override
        public int getCount() {
            return memories.size();
        }
    }
}
