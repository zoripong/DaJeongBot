package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateBot;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateName;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeBotActivity extends AppCompatActivity {
    private final String TAG = "ChangeBotActivity";
    private ImageView ivBot1;
    private ImageView ivBot2;
    private ImageView ivBot3;
    private ImageView ivBot4;
    //    TextView tvBotChange;
    private int check=-1; //어떤 캐릭터를 선택했는지 값 저장
    /*
    * {
      "account_id": 32,
      "new_bot_type": 2
    }
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bot);

        getUserBot();

        //각각 캐릭터 아이콘을 클릭한 경우
        ivBot1 = (ImageView) findViewById(R.id.ivBot1);
        ivBot2 = (ImageView) findViewById(R.id.ivBot2);
        ivBot3 = (ImageView) findViewById(R.id.ivBot3);
        ivBot4 = (ImageView) findViewById(R.id.ivBot4);
        findViewById(R.id.ivBot1).setOnClickListener(onClickListener);
        findViewById(R.id.ivBot2).setOnClickListener(onClickListener);
        findViewById(R.id.ivBot3).setOnClickListener(onClickListener);
        findViewById(R.id.ivBot4).setOnClickListener(onClickListener);
        findViewById(R.id.tvBotChange).setOnClickListener(onClickListener);

    }

    private void getUserBot(){
        int bot = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");
        Log.e(TAG, "봇,, : "+bot);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ivBot1:
                    ivBot1.setImageResource(R.drawable.selected_bot1_ic);
                    ivBot2.setImageResource(R.drawable.ic_char2);
                    ivBot3.setImageResource(R.drawable.ic_char3);
                    ivBot4.setImageResource(R.drawable.ic_char4);
                    check=0;
                    break;
                case R.id.ivBot2:
                    ivBot1.setImageResource(R.drawable.ic_char1);
                    ivBot2.setImageResource(R.drawable.selected_bot2_ic);
                    ivBot4.setImageResource(R.drawable.ic_char4);
                    ivBot4.setImageResource(R.drawable.ic_char4);
                    check=1;
                    break;
                case R.id.ivBot3:
                    ivBot1.setImageResource(R.drawable.ic_char1);
                    ivBot2.setImageResource(R.drawable.ic_char2);
                    ivBot3.setImageResource(R.drawable.selected_bot3_ic);
                    ivBot4.setImageResource(R.drawable.ic_char4);
                    check=2;
                    break;
                case R.id.ivBot4:
                    ivBot1.setImageResource(R.drawable.ic_char1);
                    ivBot2.setImageResource(R.drawable.ic_char2);
                    ivBot3.setImageResource(R.drawable.ic_char3);
                    ivBot4.setImageResource(R.drawable.selected_bot4_ic);
                    check=3;
                    break;
                case R.id.tvBotChange:
                    if(check==-1){
                        Toast.makeText(getApplicationContext(), "캐릭터를 선택해주세요!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        updateBotType();
                    }
            }

        }
    };


    public void updateBotType() {
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");
        if (!(accountId == null && accountId.equals(""))) {
            final RequestUpdateBot param = new RequestUpdateBot(Integer.parseInt(accountId), Integer.toString(check));
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().updateBots(param);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("status")) {
                        if (response.body().get("status").getAsString().equals("Success")) {
                            Log.e(TAG, "챗봇 변경 성공");
                            Toast.makeText(getApplicationContext(), "성공적으로 챗봇을 변경했습니다", Toast.LENGTH_LONG).show();
                            CustomSharedPreference.getInstance(getApplicationContext(), "user_info")
                                    .savePreferences("bot_type",check);
                            int charType = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");
                            Log.e(TAG,"바뀐 봇 타입 : "+charType);
//                            Intent intent = new Intent(ChangeBotActivity.this, SettingActivity.class);
//                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "서버의 문제로 챗봇 변경에 실패하였습니다.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t != null) {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 챗봇을 변경하지 못하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
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
        mCurrentScreen.setText("챗봇 변경");
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
