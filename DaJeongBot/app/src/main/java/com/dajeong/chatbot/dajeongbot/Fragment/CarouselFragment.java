package com.dajeong.chatbot.dajeongbot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.customize.SwipeViewPager;
import com.dajeong.chatbot.dajeongbot.model.Memory;

/**
 * Created by s2017 on 2018-08-31.
 */

public class CarouselFragment extends Fragment {
    private static final String LIST_MEMORY = null;
    private static int mPosition;
    private TextView mTvContent;

    private String Content;
    private int count=0;

    public CarouselFragment() {

    }

    public static CarouselFragment newInstance(int position, Memory memory) {
        mPosition = position;
        CarouselFragment fragment = new CarouselFragment();
        Bundle args = new Bundle();
        args.putString(LIST_MEMORY, memory.getContent());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Content = getArguments().getString(LIST_MEMORY);
        }
    }


    // 현재 viewpager 의 position
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("ResourceType") final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_test, container, false);

        mTvContent = rootView.findViewById(R.id.textview);

        mTvContent.setText(Content);

        return rootView;
    }
}