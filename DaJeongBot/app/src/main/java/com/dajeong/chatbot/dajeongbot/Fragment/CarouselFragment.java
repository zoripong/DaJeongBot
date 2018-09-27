package com.dajeong.chatbot.dajeongbot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.model.Memory;

/**
 * Created by s2017 on 2018-08-31.
 */

public class CarouselFragment extends Fragment {
    private static final String  LIST_MEMORY = null;
    private ImageView mIvContent;

    private int Content;
    private int count=0;

    public CarouselFragment() {

    }

    public static CarouselFragment newInstance(int position, Memory memory) {
        CarouselFragment fragment = new CarouselFragment();
        Bundle args = new Bundle();
        args.putString(LIST_MEMORY, memory.getImage());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Content = getArguments().getInt(LIST_MEMORY);
        }
    }


    // 현재 viewpager 의 position
    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("ResourceType") final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_item_carousel, container, false);

        mIvContent = rootView.findViewById(R.id.ivCarousel);

//        mIvContent.setImageResource(Content);
        Glide.with(rootView)
                .load(R.drawable.no_image)
                .into(mIvContent);
        mIvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "잠시만요! 일정과 함께 사진을 저장할 수 있도록 금방 돌아올게요!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}