package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dajeong.chatbot.dajeongbot.R;

// 챗봇 선택 activity
public class SelectBotActivity extends AppCompatActivity  {
//    private final int Chat1Fragment=1;
//    private final int Chat2Fragment=2;
//
//
//    private Button btn_select_bot_1,btn_select_bot_2,btn_select_bot_3,btn_select_bot_4;
//    private ImageView img_select_dot_1,img_select_dot_2,img_select_dot_3,img_select_dot_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bot);

//        btn_select_bot_1 = findViewById(R.id.btn_chatbot_1);
//        btn_select_bot_2 = findViewById(R.id.btn_chatbot_2);
//        btn_select_bot_3 = findViewById(R.id.btn_chatbot_3);
//        btn_select_bot_4 = findViewById(R.id.btn_chatbot_4);
//
//        img_select_dot_1 = findViewById(R.id.img_select_dot_1);
//        img_select_dot_2 = findViewById(R.id.img_select_dot_2);
//        img_select_dot_3 = findViewById(R.id.img_select_dot_3);
//        img_select_dot_4 = findViewById(R.id.img_select_dot_4);
//
//        btn_select_bot_1.setOnClickListener(this);
//        btn_select_bot_2.setOnClickListener(this);
//        btn_select_bot_3.setOnClickListener(this);
//        btn_select_bot_4.setOnClickListener(this);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBotActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        callFragment(Chat1Fragment);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_chatbot_1:
//                callFragment(Chat1Fragment);
//                break;
//            case R.id.btn_chatbot_2:
//                callFragment(Chat2Fragment);
//                break;
//        }
//    }
//    private void callFragment(int fragment_no){
//        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        switch (fragment_no){
//            case 1:
//                Chat1Fragment chat1Fragment = new Chat1Fragment();
//                transaction.replace(R.id.fragment, chat1Fragment);
//                transaction.commit();
//                break;
//            case 2:
//                Chat2Fragment chat2Fragment = new Chat2Fragment();
//                transaction.replace(R.id.fragment, chat2Fragment);
//                transaction.commit();
//                break;
//
//        }
//    }
}
