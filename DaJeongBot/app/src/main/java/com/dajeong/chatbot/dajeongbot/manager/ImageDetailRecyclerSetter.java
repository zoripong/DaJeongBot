package com.dajeong.chatbot.dajeongbot.manager;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dajeong.chatbot.dajeongbot.adapter.ImageDetailRecyclerAdapter;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;

import java.util.List;


public class ImageDetailRecyclerSetter {
    List<GalleryImage> items;
    ImageDetailRecyclerAdapter adapter;

    Context context;
    Activity nowActivity;

    public ImageDetailRecyclerSetter(Context context, Activity nowActivity) {
        this.context = context;
        this.nowActivity = nowActivity;
    }

    public boolean setRecyclerCardView(RecyclerView recyclerView, List<GalleryImage> imageArrayList){
        items = imageArrayList;
        adapter = new ImageDetailRecyclerAdapter(context, nowActivity, items);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        return true;
    }

    public ImageDetailRecyclerAdapter getAdapter(){
        return adapter;
    }
}
