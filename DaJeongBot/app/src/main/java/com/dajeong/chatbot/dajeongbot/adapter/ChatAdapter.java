package com.dajeong.chatbot.dajeongbot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.dajeong.chatbot.dajeongbot.activity.ImageDetailActivity;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.alias.ChatHolderType;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.AWSMobileController;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.fragment.CarouselFragment;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.model.Memory;
import com.dajeong.chatbot.dajeongbot.model.Slot;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "ChatAdapter";
    private int BUTTON_ID = 8000;

    private HashSet<Button> buttons;
    private LinkedList<Chat> mChats;
    private Context mContext;
    private FragmentManager mFragmentManager;
    //        HashMap<Integer, Integer> mViewPagerState = new HashMap<>();
    private View vBot;
    private View vUser;
    private View vSlot;
    private View vCarousel;
    private View vBotImage;
    private View vUserImage;

    public ChatAdapter(FragmentManager fragmentManager, LinkedList<Chat> chats, Context context) {
        this.mFragmentManager = fragmentManager;
        this.mChats = chats;
        this.mContext = context;
        buttons = new HashSet<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                vBot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_default, parent, false);
                return new ChatBotHolder(vBot);
            case 1:
                vUser = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_default, parent, false);
                return new ChatUserHolder(vUser);
            case 2:
                vSlot = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_slot, parent, false);
                return new ChatBotSlotHolder(vSlot);
            case 3:
                vCarousel = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_carousel, parent, false);
                return new ChatBotCarouselHolder(vCarousel);
            case 4:
                vBotImage = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_bot_image, parent, false);
                return new ChatBotImageHolder(vBotImage);
            case 5:
                vUserImage = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user_image, parent, false);
                return new ChatUserImageHolder(vUserImage);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final Chat chat = mChats.get(position);
//        Log.e(TAG, "view type is "+holder.getItemViewType() + "/" +chat.getContent());

        boolean buttonEnable = false;
        if(position==mChats.size()-1){
            switch (holder.getItemViewType()) {
                case ChatHolderType.CHAT_BOT_HOLDER:
                case ChatHolderType.CHAT_USER_HOLDER:
                case ChatHolderType.CHAT_BOT_IMAGE_HOLDER:
                case ChatHolderType.CHAT_USER_IMAGE_HOLDER:
//                    Toast.makeText(mContext, "ET enable", Toast.LENGTH_LONG).show();
//                    Toast.makeText(mContext, "버튼 비활성화", Toast.LENGTH_LONG).show();;
                    ((MainActivity)mContext).setSendEditText(true);
                    buttonEnable = false;
                    break;

                case ChatHolderType.CHAT_BOT_SLOT_HOLDER: // chatBotSlotHolder
                case ChatHolderType.CHAT_BOT_CAROUSEL_HOLDER: // chatBotCarouselHolder
// /                    Toast.makeText(mContext, "ET disable", Toast.LENGTH_LONG).show();;
//                    Toast.makeText(mContext, "버튼 활성화", Toast.LENGTH_LONG).show();;
                    ((MainActivity)mContext).setSendEditText(false);
                    buttonEnable = true;
                    break;
            }
        }

        switch (holder.getItemViewType()) {

            case ChatHolderType.CHAT_BOT_HOLDER: //ChatBotHolder
                ChatBotHolder chatBotHolder = (ChatBotHolder) holder;
                chatBotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotHolder.mTvContent.setText(chat.getContent());
                chatBotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case ChatHolderType.CHAT_USER_HOLDER: //chatUserHolder
                ChatUserHolder chatUserHolder = (ChatUserHolder) holder;
                chatUserHolder.mTvContent.setText(chat.getContent());
                chatUserHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                break;
            case ChatHolderType.CHAT_BOT_SLOT_HOLDER: // chatBotSlotHolder
                ChatBotSlotHolder chatBotSlotHolder = (ChatBotSlotHolder) holder;
                chatBotSlotHolder.mTvContent.setText(chat.getContent());
                chatBotSlotHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotSlotHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotSlotHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
//                Log.e(TAG, chat.getContent()+"slot의 채트 타입은 :" + chat.getChatType());
                if (!chatBotSlotHolder.hasBtn) {
                    chatBotSlotHolder.setHasBtn(true);
                    createSlotButtons(chatBotSlotHolder.mRootLayout, chat.getSlotList(), chat.getChatType(), buttonEnable);
                }

                break;
            case ChatHolderType.CHAT_BOT_CAROUSEL_HOLDER: // chatBotCarouselHolder
                final ChatBotCarouselHolder chatBotCarouselHolder = (ChatBotCarouselHolder) holder;
                chatBotCarouselHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotCarouselHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotCarouselHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                chatBotCarouselHolder.mTvHead.setText(chat.getContent());
                final ArrayList<Memory> memories = chat.getCarouselList();
                chatBotCarouselHolder.setMemories(memories);

                final CarouselPagerAdapter carouselPagerAdapter = new CarouselPagerAdapter(mFragmentManager, memories);
                chatBotCarouselHolder.mVpimage.setAdapter(carouselPagerAdapter);// viewpager 에 adapter 달아주기
                chatBotCarouselHolder.mTvSchedule.setText("1번째 일정");
                chatBotCarouselHolder.mBtText.setText(memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getContent());
                chatBotCarouselHolder.mLiPrevious.setOnClickListener(chatBotCarouselHolder.CarouselBtnHandler);
                chatBotCarouselHolder.mLiNext.setOnClickListener(chatBotCarouselHolder.CarouselBtnHandler);
                chatBotCarouselHolder.mBtPrevious.setOnClickListener(chatBotCarouselHolder.CarouselBtnHandler);
                chatBotCarouselHolder.mBtNext.setOnClickListener(chatBotCarouselHolder.CarouselBtnHandler);
                chatBotCarouselHolder.mVpimage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        chatBotCarouselHolder.setCount(position);

                    }

                    @Override
                    public void onPageSelected(final int position) {
                        chatBotCarouselHolder.setCount(position);
                        chatBotCarouselHolder.CarouselCondition();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                chatBotCarouselHolder.mBtText.setEnabled(buttonEnable);
                // 버튼 선택
                chatBotCarouselHolder.mBtText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(mContext, memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getEventId()+"/"+memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getContent(), Toast.LENGTH_LONG).show();
                        int accountId = Integer.parseInt(CustomSharedPreference.getInstance(mContext, "user_info").getStringPreferences("id"));
                        String content = memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getContent();
                        int chatType = ChatType.MEMORY_CHAT;
                        long time = System.currentTimeMillis();
                        int isBot = 0;
                        String param = createResponseJson(chat.getCarouselList());

                        ((MainActivity)mContext).setSelectIndex(memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getEventId());
                        ((MainActivity)mContext).setJsonResponse(new JsonParser().parse(param).getAsJsonObject());
                        ((MainActivity)mContext).sendMessage(accountId, content, NodeType.SPEAK_NODE, chatType, String.valueOf(time), isBot);

                        mChats.addLast(new Chat(NodeType.SPEAK_NODE, null, memories.get(chatBotCarouselHolder.mVpimage.getCurrentItem()).getContent(), String.valueOf(System.currentTimeMillis())));

                        notifyDataSetChanged();
                    }
                });
                break;

            case ChatHolderType.CHAT_BOT_IMAGE_HOLDER: // chatBotImageHolder
                // 챗봇이 보낸 이미지 채팅 ui
                ChatBotImageHolder chatBotImageHolder = (ChatBotImageHolder) holder;
                chatBotImageHolder.mIvSenderProfile.setVisibility(View.VISIBLE);
                chatBotImageHolder.mIvSenderProfile.setImageResource(chat.getSender().getProfile());
                chatBotImageHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                chatBotImageHolder.showImage(chat.getContent());
                break;
            case ChatHolderType.CHAT_USER_IMAGE_HOLDER: // chatUserImageHolder
                // 사용자가 보낸 이미지 채팅 ui
                ChatUserImageHolder chatUserImageHolder = (ChatUserImageHolder) holder;
                chatUserImageHolder.mTvTime.setText(new SimpleDateFormat("a HH:mm", Locale.KOREA).format(new Date(Long.parseLong(chat.getTime()))));
                chatUserImageHolder.showImage(chat.getContent());
                break;
            default:

        }


    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        //
        if (mChats.get(position).getSender() != null) {
            // 챗봇이 전송
            if (mChats.get(position).getNodeType() == NodeType.SLOT_NODE
                    && mChats.get(position).getSlotList() != null) {
                // slot node 일 경우
                return ChatHolderType.CHAT_BOT_SLOT_HOLDER;
            }

            if (mChats.get(position).getNodeType() == NodeType.CAROUSEL_NODE
                    && mChats.get(position).getCarouselList() != null) {
                // carousel node 일 경우
                return ChatHolderType.CHAT_BOT_CAROUSEL_HOLDER;
            }
            if (mChats.get(position).getNodeType() == NodeType.IMAGE_NODE) {
                // image  node 일 경우
                return ChatHolderType.CHAT_BOT_IMAGE_HOLDER;
            }


            return ChatHolderType.CHAT_BOT_HOLDER;
        } else {
            // 사용자가 전송
            if (mChats.get(position).getNodeType() == NodeType.IMAGE_NODE) {
                // image node 일 경우
                return ChatHolderType.CHAT_USER_IMAGE_HOLDER;
            }
            return ChatHolderType.CHAT_USER_HOLDER;
        }
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }


    private void createSlotButtons(LinearLayout layout, final ArrayList<Slot>slotArrayList, int chatType, boolean status) {
        Log.e(TAG, "createSlotButtons : "+ slotArrayList.size());
        Log.e(TAG, "slotArrayList.toString(): " + slotArrayList.toString());

        for (int i = 0; i < slotArrayList.size(); i++) {
            //{'value': 'yes', 'id': '1', 'type': 'btn', 'label': '응'}
            final Button myButton = new Button(mContext);
            buttons.add(myButton);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, mContext.getResources().getDisplayMetrics());
            // 가로, 세로, 마진
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
            lp.setMargins(15, 5, 15, 10);
            myButton.setLayoutParams(lp);
            myButton.setText(slotArrayList.get(i).getLabel());
            myButton.setEnabled(status);



            // 스타일 지정
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(android.R.attr.borderlessButtonStyle, typedValue, true);

            // 배경 지정
            final int version = Build.VERSION.SDK_INT;
            if (version >= 21) {
                myButton.setBackground(ContextCompat.getDrawable(mContext, R.drawable.chatbot_tv_slot_custom));
            } else {
                myButton.setBackground(mContext.getResources().getDrawable(R.drawable.chatbot_tv_slot_custom));
            }
            BUTTON_ID++;
            myButton.setId(BUTTON_ID);
            //            LinearLayout layout = (LinearLayout) findViewById(R.id.myDynamicLayout);
            layout.addView(myButton);
            if(chatType == 5)
                chatType = 4;
            else if(chatType == 4)
                chatType = 5;
            final int finalI = i;
            final int finalChatType = chatType;
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
//                    Toast.makeText(mContext, myButton.getText().toString(), Toast.LENGTH_LONG).show();
                    int accountId = Integer.parseInt(CustomSharedPreference.getInstance(mContext, "user_info").getStringPreferences("id"));
//                     chattype == > ChatType.REGISTER_CHAT or ChatType.QUESTION_SCHEDULE_SELECT_CHAT
                    Log.e(TAG, "그래.. 넌 뭐니..?"+ finalChatType);


                    ((MainActivity) mContext)
                            .sendMessage(accountId,
                                    slotArrayList.get(finalI).getValue()+":"+slotArrayList.get(finalI).getLabel(),
                                    NodeType.SPEAK_NODE,
                                    finalChatType,
                                    String.valueOf(System.currentTimeMillis()),
                                    0);
                    mChats.add(new Chat(NodeType.SPEAK_NODE, null, slotArrayList.get(finalI).getLabel(), String.valueOf(System.currentTimeMillis())));
                    notifyDataSetChanged();
                }
            });
        }

        Iterator<Button> iterator = buttons.iterator();
        while(iterator.hasNext()){
            Log.e(TAG, "id : "+iterator.next().getId());
        }

    }

    private String createResponseJson(ArrayList<Memory> memories){
        StringBuffer events = new StringBuffer();
        for(int i = 0; i<memories.size()-1; i++){
            events.append("{");
            events.append("\"id\": ");
            events.append(memories.get(i).getEventId());
            events.append(",");

            events.append("\"event_image\": ");
            events.append("\""+memories.get(i).getImage()+"\"");
            events.append(",");

            events.append("\"event_detail\": ");
            events.append("\""+memories.get(i).getContent()+"\"");
            events.append(",");

            events.append("\"detail\": ");
            events.append("\""+memories.get(i).getDetail()+"\"");
            events.append(",");

            events.append("\"review\": ");
            events.append("\""+memories.get(i).getReview()+"\"");
            if(i == memories.size() -2){
                events.append("}");
            }else{
                events.append("},");
            }
        }

        return "{" +
                "\"select_idx\": "+ ((MainActivity)mContext).getSelectIndex() + "," +
                "\"events\": ["+ events.toString() +
                "]}";

    }

    private class ChatBotHolder extends RecyclerView.ViewHolder {
        ImageView mIvSenderProfile;
        //TextView mTvSenderName;
        TextView mTvContent;
        TextView mTvTime;

        ChatBotHolder(View itemView) {
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

        ChatUserHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tvContentUser);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
        }
    }

    private class ChatBotSlotHolder extends RecyclerView.ViewHolder {
        LinearLayout mRootLayout;
        ImageView mIvSenderProfile;
        TextView mTvTime;
        TextView mTvContent;

        boolean hasBtn;

        ChatBotSlotHolder(View itemView) {
            super(itemView);
            mRootLayout = itemView.findViewById(R.id.ll_dynamic_btns);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            hasBtn = false;
            mTvTime = itemView.findViewById(R.id.tvTime);
            mTvContent = itemView.findViewById(R.id.tvContent);
        }

        void setHasBtn(boolean hasBtn) {
            this.hasBtn = hasBtn;
        }
    }

    private class ChatBotCarouselHolder extends RecyclerView.ViewHolder {
        TextView mTvHead;
        ImageView mIvSenderProfile;
        TextView mTvSchedule;
        Button mBtText;
        ViewPager mVpimage;
        TextView mTvTime;
        Button mBtNext, mBtPrevious;
        ImageView mIvImage;// layout view_item_carousel.xmlousel.xml
        LinearLayout mLiPrevious, mLiNext;
        ArrayList<Memory> memories;
        int count = 0;
        ChatBotCarouselHolder(View itemView) {
            super(itemView);
            mTvHead = itemView.findViewById(R.id.tvContent);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mTvSchedule = itemView.findViewById(R.id.tvSchedule);
            mBtText = itemView.findViewById(R.id.btText);
            mVpimage = itemView.findViewById(R.id.vp);
            mBtNext = itemView.findViewById(R.id.btn_carousel_next);
            mBtPrevious = itemView.findViewById(R.id.btn_carousel_previous);
            mBtPrevious.setVisibility(View.INVISIBLE);
            mTvTime = itemView.findViewById(R.id.tvTime);
            mLiNext = itemView.findViewById(R.id.linear_next);
            mLiPrevious = itemView.findViewById(R.id.linear_previous);
            mIvImage = itemView.findViewById(R.id.ivCarousel);
        }

        void setMemories(ArrayList<Memory> memories){
            this.memories = memories;
        }
        public int getCount(){
            return count;
        }

        public void setCount(int count){
            this.count = count;
        }
        View.OnClickListener CarouselBtnHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_carousel_previous:
                    case R.id.linear_previous:
                        if(count > 0)
                            count--;
                        break;
                    case R.id.btn_carousel_next:
                    case R.id.linear_next:
                        if(count < memories.size())
                            count++;
                        break;
                }
                CarouselCondition();

            }
        };

        private void CarouselCondition() {
            if (count == 0) {
                mLiPrevious.setVisibility(View.INVISIBLE);
                mLiNext.setVisibility(View.VISIBLE);
                mBtPrevious.setVisibility(View.INVISIBLE);
                mBtNext.setVisibility(View.VISIBLE);
            } else if (count == memories.size() - 1) {
                mLiPrevious.setVisibility(View.VISIBLE);
                mLiNext.setVisibility(View.INVISIBLE);
                mBtNext.setVisibility(View.INVISIBLE);
                mBtPrevious.setVisibility(View.VISIBLE);
            } else {
                mLiPrevious.setVisibility(View.VISIBLE);
                mLiNext.setVisibility(View.VISIBLE);
                mBtNext.setVisibility(View.VISIBLE);
                mBtPrevious.setVisibility(View.VISIBLE);
            }
            mVpimage.setCurrentItem(count);
            mTvSchedule.setText(count + 1 + "번째 일정");
            mBtText.setText(memories.get(count).getContent());
        }


    }

    private class ChatBotImageHolder extends RecyclerView.ViewHolder {
        ImageView mIvSenderProfile;
        ImageView mIvImage;
        TextView mTvTime;
        String filePath;
        public ChatBotImageHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cl_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra("IMAGE_SRC", filePath);
                    intent.putExtra("SEND_TIME", mTvTime.getText().toString());
                    mContext.startActivity(intent);
                }
            });
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            mIvImage = itemView.findViewById(R.id.ivBotImage);
            mTvTime = itemView.findViewById(R.id.tvTime);
            itemView.findViewById(R.id.ivDownload).setVisibility(View.GONE);
            itemView.findViewById(R.id.ivDownload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(filePath!= null){
                        Log.e(TAG, filePath+"다운로드");
                        AWSMobileController.getInstance(mContext).downloadWithTransferUtility(filePath);
                    }
                }
            });
        }

        @SuppressLint("ResourceType")
        private void showImage(String imgUrl) {
            filePath= imgUrl;
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(mIvImage);
            Glide.with(ChatBotImageHolder.this.itemView)
                    .load(imgUrl)
                    .thumbnail(Glide.with(ChatBotImageHolder.this.itemView).load(R.raw.img_loading))
                    //.apply(new RequestOptions().placeholder(R.raw.image_loading))
                    .into(imageViewTarget);
        }
    }

    private class ChatUserImageHolder extends RecyclerView.ViewHolder {
        ImageView mIvImage;
        TextView mTvTime;
        String filePath;

        public ChatUserImageHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.chat_root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ImageDetailActivity.class);
                    intent.putExtra("IMAGE_SRC", filePath);
                    intent.putExtra("SEND_TIME", mTvTime.getText().toString());
                    mContext.startActivity(intent);
                }
            });

            mIvImage = itemView.findViewById(R.id.ivUserImage);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
            itemView.findViewById(R.id.ivDownload).setVisibility(View.GONE);
            itemView.findViewById(R.id.ivDownload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(filePath!= null){
                    Log.e(TAG, filePath+"다운로드");
                        AWSMobileController.getInstance(mContext).downloadWithTransferUtility(filePath);
                    }
                }
            });
        }

        @SuppressLint("ResourceType")
        private void showImage(String imgUrl) {
            filePath = imgUrl;
            DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(mIvImage);
            Glide.with(ChatUserImageHolder.this.itemView)
                    .load(imgUrl)
                    .thumbnail(Glide.with(ChatUserImageHolder.this.itemView).load(R.raw.img_loading))
//                    .apply(new RequestOptions().placeholder(R.raw.image_loading))
                    .into(imageViewTarget);
        }
    }

    public class CarouselPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Memory> memories;

        CarouselPagerAdapter(FragmentManager fm, ArrayList<Memory> memories) {
            super(fm);
            this.memories = memories;
        }

        @Override
        public Fragment getItem(final int position) {
            return CarouselFragment.newInstance(position, memories.get(position));
        }

        @Override
        public int getCount() {
            return memories.size();
        }
    }
}
