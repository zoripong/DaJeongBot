package com.dajeong.chatbot.dajeongbot.activity.setting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.SettingActivity;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.customize.PasswordDialog;
import com.dajeong.chatbot.dajeongbot.customize.ResetDialog;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateName;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdatePassword;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private final String TAG = "ChangePasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        final EditText etPassword = findViewById(R.id.editPassword);
        findViewById(R.id.tvPasswordChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( etPassword.getText().toString().trim().length() > 0 ){
                    PasswordDialog customDialog = new PasswordDialog(ChangePasswordActivity.this, etPassword.getText().toString().trim());
                    customDialog.callFunction();
                }else{
                    YoYo.with(Techniques.Shake).playOn(etPassword);
                }

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewActionBar = inflater.inflate(R.layout.custom_back_actionbar, null);

        actionBar.setCustomView(viewActionBar);

        // 액션바 배경 색 바꾸기
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        TextView mCurrentScreen = (TextView) findViewById(R.id.tvCurrentScreen);
        mCurrentScreen.setText("비밀번호 변경");
        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
}
