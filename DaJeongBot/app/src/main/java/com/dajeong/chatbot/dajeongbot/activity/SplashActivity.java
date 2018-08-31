package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.fcm.MyFirebaseInstanceIDService;
import com.google.firebase.iid.FirebaseInstanceId;

// intro activity
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        test();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id").equals("")){
                    // 로그인 내역이 남아있다면
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                // 처음일 경우 tutorial, 아닐 경우 LoginActivity
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                }
            }
        }, 2000);
    }

    public void test(){
        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("id", "32");
        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("bot_type", 0);

        Log.e(TAG, "firebase device token is :" + FirebaseInstanceId.getInstance().getToken());

    }
}
