package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dajeong.chatbot.dajeongbot.R;

// intro activity
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 처음일 경우 tutorial, 이미 회원일 경우 main
                Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
