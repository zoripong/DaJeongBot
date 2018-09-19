package com.dajeong.chatbot.dajeongbot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.androidanimations.library.Techniques;
public class EventAdapter extends RecyclerSwipeAdapter<EventAdapter.SimpleViewHolder> {

    private Vector<Event> mEvents;

    private Context mContext;
    private ArrayList<Event> EventInfoArrayList;

    //protected SwipeItemRecyclerMangerImpl mItemManger = new SwipeItemRecyclerMangerImpl(this);

    public EventAdapter(Context context, ArrayList<Event> objects) {
        this.mContext = context;
        this.EventInfoArrayList = objects;
    }

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        TextView tvDelete;
        TextView tvEdit;
        TextView tvScheduleWhen;
        TextView tvScheduleWhere;
        TextView tvScheduleWhat;
        ImageView ivEventImage;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvScheduleWhen = itemView.findViewById(R.id.tvScheduleWhen);
            tvScheduleWhere = itemView.findViewById(R.id.tvScheduleWhere);
            tvScheduleWhat = itemView.findViewById(R.id.tvScheduleWhat);
            ivEventImage = itemView.findViewById(R.id.ivEventImage);

        }
    }


//    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
//        SwipeLayout swipeLayout;
//        TextView textViewPos;
//        TextView textViewData;
//        Button buttonDelete;
//
//        public SimpleViewHolder(View itemView) {
//            super(itemView);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
//            textViewPos = (TextView) itemView.findViewById(R.id.position);
//            textViewData = (TextView) itemView.findViewById(R.id.text_data);
//            buttonDelete = (Button) itemView.findViewById(R.id.delete);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.d(getClass().getSimpleName(), "onItemSelected: " + textViewData.getText().toString());
//                    Toast.makeText(view.getContext(), "onItemSelected: " + textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_row_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        //String item = EventInfoArrayList.get(position);
        SimpleViewHolder myViewHolder = (SimpleViewHolder) viewHolder;
        switch (viewHolder.getItemViewType()){
            case 0: //내용
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
                myViewHolder.ivEventImage.setVisibility(View.GONE);
                break;
            case 1: //내용+사진
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
                myViewHolder.ivEventImage.setImageResource(EventInfoArrayList.get(position).drawableId);
                break;
            default:
                return;
        }
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvDelete));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                EventInfoArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, EventInfoArrayList.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted ", Toast.LENGTH_SHORT).show();
            }
        });
        //mItemManger.bind(viewHolder.itemView, position);
        mItemManger.bindView (viewHolder.itemView, position);
    }

//        @Override
//    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
//        // Event event = mEvents.get(position);
//        MyViewHolder myViewHolder = (MyViewHolder) holder;
//        switch (holder.getItemViewType()){
//            case 0: //내용
//                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
//                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
//                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
//                myViewHolder.ivEventImage.setVisibility(View.GONE);
//                break;
//            case 1: //내용+사진
//                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).scheduleWhen);
//                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).scheduleWhere);
//                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).scheduleWhat);
//                myViewHolder.ivEventImage.setImageResource(EventInfoArrayList.get(position).drawableId);
//                break;
//            default:
//                return;
//        }
//    }



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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}

