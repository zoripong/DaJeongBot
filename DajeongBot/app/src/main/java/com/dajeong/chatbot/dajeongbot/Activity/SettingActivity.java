package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dajeong.chatbot.dajeongbot.Control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.R;

// setting activity ( 사용자 정보 수정 / 챗봇 선택 / 데이터 초기화 .. )
public class SettingActivity extends AppCompatActivity {
    // TODO : 로그아웃시 로그인 정보 삭제
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomSharedPreference.getInstance(getApplicationContext(), "user_info").removeAllPreferences();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
