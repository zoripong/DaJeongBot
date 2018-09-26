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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateName;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.api.client.json.Json;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// TODO : 현재 닉네임 볼 수 있도록
public class ChangeNameActivity extends AppCompatActivity {
    private final String TAG = "ChangeNameActivity";
    EditText editName;
    String getEditName;
    /*
    * {
      "account_id": 32,
      "new_name": "한유리"
    }
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");

        editName = findViewById(R.id.editName);
        getUserName(Integer.parseInt(accountId));

        findViewById(R.id.tvNameChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditName = editName.getText().toString();
                if(getEditName.length() <= 0){//빈값이 넘어올때의 처리
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                else{
                    updateName();
                }
            }
        });
    }

    private void getUserName(int accountId){
        Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().getUserName(accountId);
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body() != null){
                    JsonObject body = response.body();
                   if(body.has("status") && "Success".equals(body.get("status").getAsString())){
                        JsonObject data = body.get("data").getAsJsonObject();
                        Log.e(TAG, "사용자 이름은 : "+data.get("name").getAsString());
                        editName.setText(data.get("name").getAsString());
                   }
                }else{
                    Toast.makeText(getApplicationContext(), "서버와의 연결에 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버와의 연결에 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
    private void updateName() {
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");
        Log.e(TAG,"accountId값 무엇일까,,,"+accountId);
        if (!(accountId == null && accountId.equals(""))) {
            final RequestUpdateName param = new RequestUpdateName(Integer.parseInt(accountId), getEditName);
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().updateName(param);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("status")) {
                        if (response.body().get("status").getAsString().equals("Success")) {
                            Log.e(TAG, "이름변경 성공");
                            Toast.makeText(getApplicationContext(), "성공적으로 이름을 변경했습니다", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(ChangeNameActivity.this, SettingActivity.class);
//                            startActivity(intent);
                            finish();
                        } else {
                            Log.e(TAG, "서버의 문제로 이름 변경에 실패하였습니다.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t != null) {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 이름을 변경하지 못하였습니다.", Toast.LENGTH_LONG).show();
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
        mCurrentScreen.setText("닉네임 변경");
        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeNameActivity.this, com.dajeong.chatbot.dajeongbot.activity.SettingActivity.class);
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
