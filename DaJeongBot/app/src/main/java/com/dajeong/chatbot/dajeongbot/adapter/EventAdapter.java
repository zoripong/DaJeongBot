package com.dajeong.chatbot.dajeongbot.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.dajeong.chatbot.dajeongbot.activity.CalendarActivity;
import com.dajeong.chatbot.dajeongbot.activity.ChangeBotActivity;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.activity.SettingActivity;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.decorators.EventDecorator;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class EventAdapter extends RecyclerSwipeAdapter<EventAdapter.SimpleViewHolder> {
    private final String TAG = "EventAdapter";

    private Context mContext;
    private ArrayList<Event> EventInfoArrayList;

    public String eventId;
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
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).getScheduleWhen());
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).getScheduleWhere());
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).getScheduleWhat());
                myViewHolder.ivEventImage.setVisibility(View.GONE);
                break;
            case 1: //내용+사진
                myViewHolder.tvScheduleWhen.setText(EventInfoArrayList.get(position).getScheduleWhen());
                myViewHolder.tvScheduleWhere.setText(EventInfoArrayList.get(position).getScheduleWhere());
                myViewHolder.tvScheduleWhat.setText(EventInfoArrayList.get(position).getScheduleWhat());
                myViewHolder.ivEventImage.setImageResource(EventInfoArrayList.get(position).getDrawableId());
                break;
            default:
                return;
        }
        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.tvDelete));
            }
        });
        viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_logout);
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = (int)(dm.widthPixels*0.8f);
                WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                wm.copyFrom(dialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                wm.width = width;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView deleteBtn = (TextView) dialog.findViewById(R.id.tvOk);
                TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
                TextView cancleBtn = (TextView) dialog.findViewById(R.id.tvCancle);
                deleteBtn.setText("수정");
                dialogText.setText("일정을 수정하시겠습니까?");

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        eventId=Integer.toString(EventInfoArrayList.get(position).getEventId());

                        Intent intent = new Intent();
                        intent.putExtra("result",eventId);
                        ((Activity)mContext).setResult(RESULT_OK,intent);
                        ((Activity)mContext).finish();
                    }
                });

                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
        viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //지우기 버튼을 클릭하면 다이얼로그를 띄움

                // custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_logout);
                DisplayMetrics dm = mContext.getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
                int width = (int)(dm.widthPixels*0.8f); //디바이스 화면 너비
                WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
                wm.copyFrom(dialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
                wm.width = width;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView deleteBtn = (TextView) dialog.findViewById(R.id.tvOk);
                TextView dialogText = (TextView) dialog.findViewById(R.id.dialogText);
                TextView cancleBtn = (TextView) dialog.findViewById(R.id.tvCancle);
                deleteBtn.setText("삭제");
                dialogText.setText("일정을 삭제하시겠습니까?");

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        //this.position = position;
                        deleteSchedule(position); //서버에 선택한 일정 삭제하기
                        mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                        EventInfoArrayList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, EventInfoArrayList.size());
                        mItemManger.closeAllItems();
                    }
                });

                cancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        mItemManger.bindView (viewHolder.itemView, position);
    }


    private void deleteSchedule(int position){
        eventId=Integer.toString(EventInfoArrayList.get(position).getEventId());
        if(eventId!=null){
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().removeEvent(Integer.parseInt(eventId));
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("status")) {
                        if (response.body().get("status").getAsString().equals("Success")) {
                            Log.e(TAG, "일정 삭제 성공");
                            Toast.makeText(getApplicationContext(), "성공적으로 일정을 삭제했습니다", Toast.LENGTH_LONG).show();
                        } else {
                            Log.e(TAG, "서버의 문제로 일정 삭제에 실패하였습니다.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t != null) {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 일정을 삭제하지 못하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
}

