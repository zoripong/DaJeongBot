package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.Adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.Model.Character;
import com.dajeong.chatbot.dajeongbot.Model.Chat;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.Date;
import java.util.Vector;

// 메인 채팅 화면 activity
public class MainActivity extends AppCompatActivity {
    //component
    private EditText etMessage;
    // recycler view
    private Vector<Chat> mChats;
    private RecyclerView mRvChatList;
    private ChatAdapter mChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        mChats = new Vector<>();
        forDebugging();
        mRvChatList = findViewById(R.id.rvChatList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvChatList.setHasFixedSize(true);
        mRvChatList.setLayoutManager(layoutManager);

        mChatAdapter = new ChatAdapter(mChats, MainActivity.this);
        mRvChatList.setAdapter(mChatAdapter);

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMessage.getText() != null && etMessage.getText().toString() != ""){
                    // TODO : 추가 할 때 애니메이션
                    // TODO : 서버에 메세지 전송
                    mChats.add(new Chat(null, etMessage.getText().toString() ,new Date()));
                    mChatAdapter.notifyDataSetChanged();
                    mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    etMessage.setText("");
                }
            }
        });

        findViewById(R.id.ivAddChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 메세지 선택
                Toast.makeText(MainActivity.this, "준비 중", Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.ivAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : 갤러리 연결
                Toast.makeText(MainActivity.this, "준비 중", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void init() {
        etMessage = findViewById(R.id.etMessage);
    }

    //FIXME : REMOVE this Method
    // TODO : 서버에서 메세지 가져오기
    private void forDebugging(){

        Character character = new Character("다정이", R.drawable.ic_char1);

        mChats.add(new Chat(character, "1번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(null, "2번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(null, "22번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(character, "3번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(character, "33번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(null, "4번째 메세지랍니다.", new Date()));
        mChats.add(new Chat(character, "5번째 메세지랍니다.", new Date()));


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
        View viewActionBar = inflater.inflate(R.layout.custom_main_actionbar, null);

        actionBar.setCustomView(viewActionBar);

        // 액션바에 백그라운드 이미지를 아래처럼 입힐 수 있습니다.
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_main_top_background));

        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                finish();
            }
        });

        viewActionBar.findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

}
