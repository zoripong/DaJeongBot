package com.dajeong.chatbot.dajeongbot.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateTime;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import java.util.Calendar;

import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeTimeActivity extends AppCompatActivity {
    private final String TAG = "ChangeTimeActivity";
    EditText etAlarm;
    EditText etTime;
    TextView mUpdateTime;
    TimePickerDialog picker;
    /*
    * {
      "account_id": 32,
      "new_notify_time": "08:00",
      "new_ask_time": "09:00"
    }
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time);

        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");

        getUserTime(Integer.parseInt(accountId));

        etAlarm=(EditText) findViewById(R.id.etScheduleAlarm);
        etAlarm.setInputType(InputType.TYPE_NULL);
        etAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog Tp = new TimePickerDialog(ChangeTimeActivity.this,R.style.TimePickerDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHourOfDay=Integer.toString(hourOfDay);
                        String sMinute=Integer.toString(minute);
                        if(hourOfDay<10){
                            sHourOfDay="0"+Integer.toString(hourOfDay);
                        }
                        if(minute<10){
                            sMinute="0"+Integer.toString(minute);
                        }
                        etAlarm.setText(sHourOfDay+ ":" +sMinute);

                    }
                },hour,minutes,false);
                Tp.show();
            }
        });

        etTime=(EditText) findViewById(R.id.etQuestionTime);
        etTime.setInputType(InputType.TYPE_NULL);
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog Tp = new TimePickerDialog(ChangeTimeActivity.this,R.style.TimePickerDialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHourOfDay=Integer.toString(hourOfDay);
                        String sMinute=Integer.toString(minute);
                        if(hourOfDay<10){
                            sHourOfDay="0"+Integer.toString(hourOfDay);
                        }
                        if(minute<10){
                            sMinute="0"+Integer.toString(minute);
                        }
                        etTime.setText(sHourOfDay+ ":" +sMinute);

                    }
                },hour,minutes,false);
                Tp.show();
            }
        });

        mUpdateTime=(TextView) findViewById(R.id.tvUpdateTime);
        mUpdateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTime();
            }
        });
    }

    public  void  updateTime() {
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");
        String newNotifyTime=etAlarm.getText().toString();
        String newAskTime=etTime.getText().toString();
        if (!(accountId == null && accountId.equals(""))) {
            final RequestUpdateTime param = new RequestUpdateTime(Integer.parseInt(accountId),newNotifyTime,newAskTime);
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().updateTimes(param);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("status")) {
                        if (response.body().get("status").getAsString().equals("Success")) {
                            Log.e(TAG, "시간 변경 성공");
                            Toast.makeText(getApplicationContext(), "성공적으로 시간을 변경했습니다", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Log.e(TAG, "서버의 문제로 시간 변경에 실패하였습니다.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t != null) {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 시간을 변경하지 못하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }

    private void getUserTime(int accountId){
        Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().getUserTimes(accountId);
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body() != null){
                    JsonObject body = response.body();
                    if(body.has("status") && "Success".equals(body.get("status").getAsString())){
                        JsonObject data = body.get("data").getAsJsonObject();
                        Log.e(TAG, "일정 알림 시간 : "+data.get("notify_time").getAsString());
                        Log.e(TAG, "일정 질문 시간 : "+data.get("ask_time").getAsString());
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
        mCurrentScreen.setText("시간 변경");
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
