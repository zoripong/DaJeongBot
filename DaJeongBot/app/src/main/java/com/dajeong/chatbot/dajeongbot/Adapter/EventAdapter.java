package com.dajeong.chatbot.dajeongbot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Vector<Event> mEvents;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;
        TextView tvScheduleWhen;
        TextView tvScheduleWhere;
        TextView tvScheduleWhat;
        ImageView ivEventImage;

        MyViewHolder(View view){
            super(view);
            tvtitle = view.findViewById(R.id.tvTItle);
            tvScheduleWhen = view.findViewById(R.id.tvScheduleWhen);
            tvScheduleWhere = view.findViewById(R.id.tvScheduleWhere);
            tvScheduleWhat = view.findViewById(R.id.tvScheduleWhat);
            ivEventImage = view.findViewById(R.id.ivEventImage);
        }
    }

    private ArrayList<Event> EventInfoArrayList;

    public  EventAdapter(ArrayList<Event> EventInfoArrayList){
        this.EventInfoArrayList = EventInfoArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_row, parent, false);

        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        // Event event = mEvents.get(position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        switch (holder.getItemViewType()){
            case 0: //제목+내용
                myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
                myViewHolder.ivEventImage.setVisibility(View.GONE);
                break;
            case 1: //제목+내용+사진
                myViewHolder.tvtitle.setText(EventInfoArrayList.get(position).eventTitle);
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
                myViewHolder.ivEventImage.setImageResource(EventInfoArrayList.get(position).drawableId);
                break;
            default:
                return;
        }


    }

    @Override
    public int getItemViewType(int position) {
        if(EventInfoArrayList.get(position).getDrawableSender()<0){
            return 0; //제목+내용
        }else{
            return 1; //제목+내용+사진
        }
    }


    @Override
    public int getItemCount() {
        return EventInfoArrayList.size();
    }

}

