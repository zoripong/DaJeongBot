package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dajeong.chatbot.dajeongbot.R;

// 사용자 정보 입력 activity
public class InputInfoActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 2;

    private ViewPager mPager;
    private PagerAdapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputInfoActivity.this, SelectBotActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
