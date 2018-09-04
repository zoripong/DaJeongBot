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
    private static final String APG_PARM1 = "parm1";
    private static int mPosition;
    private TextView mTv;

    private String mParm1;
    public CarouselFragment() {

    }

    public static CarouselFragment newInstance(int position, Memory memory) {
        mPosition = position;
        CarouselFragment fragment = new CarouselFragment();
        Bundle args = new Bundle();
        args.putString(APG_PARM1, memory.getContent());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParm1 = getArguments().getString(APG_PARM1);
        }
    }


    // 현재 viewpager 의 position
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("ResourceType") final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_test, container, false);

        Button btnNext = rootView.findViewById(R.id.btn_carousel_next);
        Button btnPrevious = rootView.findViewById(R.id.btn_carousel_previous);
        mTv = rootView.findViewById(R.id.textview);

        mTv.setText(mParm1);

        //TODO: 버튼으로 fragment 넘기기

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return rootView;
    }
}