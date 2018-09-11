package com.dajeong.chatbot.dajeongbot.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.adapter.EventAdapter;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.decorators.EventDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.MySelectorDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.OneDayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SaturdayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SundayDecorator;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// 캘린더 activity
public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener{
    // calendar view
    MaterialCalendarView widget;
    private final String TAG = "CalendarActivity";
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private int mAccountId;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM");

    private ArrayList<Event> mEvents = new ArrayList<>();

    //recyclerView
    private RecyclerView mRvEventList;
    private EventAdapter mEventAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //get select date
    private String mYear;
    private String mMonth;
    private String mDate;

    private String[] result;
    String strToday;
    private boolean isTodayEvent=false; //오늘 날짜에 이벤트가 있는지 확인

    LinearLayout scheduleList;
    LinearLayout noScheduleList;

    String shot_Day;

    TextView selectDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //recyclerView 준비
        mRvEventList = findViewById(R.id.event_root);
        mRvEventList.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRvEventList.setLayoutManager(mLayoutManager);

        //캘린더 기본 세팅
        widget = (MaterialCalendarView)findViewById(R.id.calendarView);
        widget.setOnDateChangedListener(this);
        //가장 처음 현재 날짜를 기본으로 선택
        Calendar instance = Calendar.getInstance();
        widget.setSelectedDate(instance);
        //오늘 날짜로 변경
        selectDay = (TextView) findViewById(R.id.select_day_tv);
        selectDay.setText(instance.get(Calendar.YEAR)+"."+(instance.get(Calendar.MONTH) + 1)+"."+instance.get(Calendar.DAY_OF_MONTH));

        //캘린더 header format 변경하기
        widget.setTitleFormatter(new DateFormatTitleFormatter(DATE_FORMAT));

        widget.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2015, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        widget.addDecorators(
                new MySelectorDecorator(this),
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator
        );

        scheduleList=(LinearLayout)findViewById(R.id.schedule_li);
        noScheduleList=(LinearLayout)findViewById(R.id.no_schedule_li);
        selectDay = (TextView) findViewById(R.id.select_day_tv);

        getToday();
        getEventList();

//        mEventAdapter = new EventAdapter(mEvents);
//        mRvEventList.setAdapter(mEventAdapter);
//        Log.e(TAG,"mEvents size >>"+mEventAdapter.getItemCount());
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mEvents.clear(); //리사이클러 뷰에 있는 데이터 지우기
        scheduleList.setVisibility(View.VISIBLE);
        noScheduleList.setVisibility(View.GONE);
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();

        int Year = date.getYear();
        int Month = date.getMonth() + 1;
        int Day = date.getDay();

        //월과 일이 10보다 작은 경우 앞에 0을 붙임
        mYear = String.valueOf(Year);
        if(Month<10){
            mMonth="0"+String.valueOf(Month);
        }
        else {
            mMonth = String.valueOf(Month);
        }

        if(Day<10){
            mDate="0"+String.valueOf(Day);
        }
        else {
            mDate = String.valueOf(Day);
        }

        Log.i("Year test", Year + "");
        Log.i("Month test", Month + "");
        Log.i("Day test", Day + "");

        shot_Day = Year + "." + Month + "." + Day;

        Log.i("shot_Day test", shot_Day + "");

        widget.addDecorator(new MySelectorDecorator(CalendarActivity.this));

        selectDay.setText("");
        getSchedule(); //선택한 날짜의 일정 가져오기

        mEventAdapter = new EventAdapter(mEvents);
        mRvEventList.setAdapter(mEventAdapter);
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
        mCurrentScreen.setText("캘린더");
        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

    public void getToday(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        strToday = sdf.format(c1.getTime());
        //strToday="2018-09-05";
        Log.e(TAG,"strToday >> "+strToday);

        String[] dateArray = strToday.split("-");
        mYear=dateArray[0];
        mMonth=dateArray[1];
        mDate=dateArray[2];
        shot_Day = mYear + "." + mMonth + "." + mDate;
        Log.e(TAG,"shot_Day >> "+shot_Day);
        selectDay.setText(shot_Day);

        mEventAdapter = new EventAdapter(mEvents);
        mRvEventList.setAdapter(mEventAdapter);
    }

    private  void getEventList(){
        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
        final Call<ArrayList<String>> EventList = NetRetrofit.getInstance(getApplicationContext()).getService().getDatesHavingEvent(mAccountId);

        EventList.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                ArrayList<String> dates = response.body();
                /* 특정날짜 달력에 점표시해주는곳 */
                /* 월은 0이 1월 년,일은 그대로 */
                Calendar calendar = Calendar.getInstance();
                List<CalendarDay> calendarDays = new ArrayList<>();

                for(String date : dates){
                    //Log.e(TAG, date);
                    if(date.equals(strToday)){
                        isTodayEvent=true; //오늘 일정이 있을 경우 true
                        Log.e(TAG,"오늘 일정 있음");
                        scheduleList.setVisibility(View.VISIBLE);
                        noScheduleList.setVisibility(View.GONE);
                        getSchedule();
                    }
                    else{ //일정 없음
                        Log.e(TAG,"오늘 일정 없음");
                    }
                    String[] time = date.split("-");
                    calendar.set(Integer.parseInt(time[0]),Integer.parseInt(time[1]) -1 ,Integer.parseInt(time[2]));
                    calendarDays.add(CalendarDay.from(calendar));
                }

                widget.addDecorator(new EventDecorator(R.color.colorAccent, calendarDays,CalendarActivity.this));

            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                if(t!=null){
                    Log.e(TAG, t.toString());
                }
            }
        });
    }
    private void getSchedule(){
        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));

        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getEvent(mAccountId,mYear,mMonth,mDate);
        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if(response==null)
                    Log.i(TAG, "response값이 null");
                else{
                    Log.i(TAG, "response is not null :"+response.toString());
                }
                ArrayList<JsonObject> body = response.body();

                if(body==null)
                    Log.i(TAG, "body값이 null");
                else{
                    Log.i(TAG, "body is not null :"+response.toString());
                }

                for(JsonObject json : body){
                    //정보 가져오기
                    mEvents.add(new Event(json.get("id").getAsInt(), json.get("schedule_what").getAsString(), json.get("schedule_when").getAsString(), json.get("schedule_where").getAsString(),-1)); //이미지 일단 넣어둠
                }

                Log.e(TAG,"정보 가져오기"+mEvents);
                Log.e(TAG,"mEvents size >>"+mEvents.size());

                if(body.size() == 0){
                    Log.e(TAG, "event info is not exist");
                    scheduleList.setVisibility(View.GONE);
                    noScheduleList.setVisibility(View.VISIBLE);
                }else{
                    selectDay.setText(shot_Day);
                }
                mEventAdapter.notifyDataSetChanged();

                Log.e(TAG,"mEvents size?? >>"+ mEventAdapter.getItemCount());
//                mRvEventList.scrollToPosition(mEventAdapter.getItemCount() - 1);
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                if(t!=null){
                    Log.e(TAG, t.toString());
                }
            }
        });

    }
}
