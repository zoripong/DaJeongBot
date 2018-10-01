package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.dajeong.chatbot.dajeongbot.R;

public class RequirementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement);

        Glide.with(this).load("https://bit.ly/2zHj9ji").into((ImageView) findViewById(R.id.ivImage));

        final EditText etContent = findViewById(R.id.etRequirement);

        findViewById(R.id.btnRequirement).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etContent.getText().toString().trim().length() > 0){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    // email setting 배열로 해놔서 복수 발송 가능
                    String[] address = {"devuri404@gmail.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL, address);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"[다정봇] 다정봇에 문제가 생겼습니다!");
                    intent.putExtra(Intent.EXTRA_TEXT, etContent.getText().toString());
                    startActivity(intent);
                }else {
                    YoYo.with(Techniques.Shake).playOn(etContent);
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
        mCurrentScreen.setText("오류사항 문의");
        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ChangeBotActivity.this, SettingActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }
}
