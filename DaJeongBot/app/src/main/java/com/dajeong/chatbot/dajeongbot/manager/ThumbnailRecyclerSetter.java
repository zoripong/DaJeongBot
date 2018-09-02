package com.dajeong.chatbot.dajeongbot.manager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dajeong.chatbot.dajeongbot.adapter.ThumbnailRecyclerAdapter;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;

import java.util.List;


public class ThumbnailRecyclerSetter {
    List<GalleryImage> items;
    ThumbnailRecyclerAdapter adapter;

    Context context;
    Activity nowActivity;

    public ThumbnailRecyclerSetter(Context context, Activity nowActivity) {
        this.context = context;
        this.nowActivity = nowActivity;
    }

    public boolean setRecyclerCardView(RecyclerView recyclerView, List<GalleryImage> imageArrayList){
        items = imageArrayList;
        adapter = new ThumbnailRecyclerAdapter(context, nowActivity, items);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(nowActivity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        return true;
    }

}
