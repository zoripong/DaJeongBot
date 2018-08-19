package com.dajeong.chatbot.dajeongbot.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.Model.Event;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.ArrayList;
import java.util.Vector;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Vector<Event> mEvents;

//    public EventAdapter(Vector<Event> events){
//        this.mEvents = events;
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;
        TextView tvContent;
        ImageView ivPicture;

        MyViewHolder(View view){
            super(view);
            tvtitle = view.findViewById(R.id.title_tv);
            tvContent = view.findViewById(R.id.content_tv);
            ivPicture = view.findViewById(R.id.content_img_tv);
        }
    }

    private ArrayList<Event> EventInfoArrayList;
    public EventAdapter(ArrayList<Event> EventInfoArrayList){
        this.EventInfoArrayList = EventInfoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_row, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;

//        myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
//        myViewHolder.tvContent.setText(EventInfoArrayList.get(position).eventCotent);
//        myViewHolder.ivPicture.setImageResource(EventInfoArrayList.get(position).drawableId);

//////////////////////////
        //OnlyEventTitle eventTitle = mEvents.get(position);
        switch (holder.getItemViewType()){
            case 0: //제목
                myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
                myViewHolder.tvContent.setVisibility(View.GONE);
                myViewHolder.ivPicture.setVisibility(View.GONE);
                break;
            case 1: //제목+내용
                myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
                myViewHolder.tvContent.setText(EventInfoArrayList.get(position).eventCotent);
                myViewHolder.ivPicture.setVisibility(View.GONE);
               // myViewHolder.ivPicture.setImageResource(EventInfoArrayList.get(position).drawableId);
                break;

            case 2: //제목+내용+사진
                myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
                myViewHolder.tvContent.setText(EventInfoArrayList.get(position).eventCotent);
                myViewHolder.ivPicture.setImageResource(EventInfoArrayList.get(position).drawableId);
                break;
            default:
                return;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(EventInfoArrayList.get(position).getContentSender()!=null){
            if(EventInfoArrayList.get(position).getDrawableSender()<0){
                return 1; //제목 + 내용
            }
            else
                return 2; //제목 + 내용 + 사진
        }else{
            return 0; //제목
        }


    }


    @Override
    public int getItemCount() {
        return EventInfoArrayList.size();
    }

    private class EventTitleHolder extends RecyclerView.ViewHolder{
        ImageView mIvSenderProfile;
        //TextView mTvSenderName;
        TextView mTvContent;
        TextView mTvTime;

        public EventTitleHolder(View itemView) {
            super(itemView);
            mIvSenderProfile = itemView.findViewById(R.id.ivSenderProfile);
            //mTvSenderName = itemView.findViewById(R.id.tvSenderName);
            mTvContent = itemView.findViewById(R.id.tvContent);
            mTvTime = itemView.findViewById(R.id.tvTime);
        }
    }
    private class EventContentHolder extends RecyclerView.ViewHolder{
        TextView mTvContent;
        TextView mTvTime;

        public EventContentHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tvContentUser);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
        }
    }

    private class EventImgHolder extends RecyclerView.ViewHolder{
        TextView mTvContent;
        TextView mTvTime;

        public EventImgHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tvContentUser);
            mTvTime = itemView.findViewById(R.id.tvTimeUser);
        }
    }
}

