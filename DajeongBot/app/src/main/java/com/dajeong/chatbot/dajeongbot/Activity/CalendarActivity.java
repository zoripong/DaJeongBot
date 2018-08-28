package com.dajeong.chatbot.dajeongbot.activity;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.adapter.EventAdapter;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.decorators.EventDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.MySelectorDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.OneDayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SaturdayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SundayDecorator;
import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;
import android.content.Context;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

// 캘린더 activity
public class CalendarActivity extends AppCompatActivity implements OnDateSelectedListener{
    // calendar view
    MaterialCalendarView widget;
    private final String TAG = "CalendarActivity";
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    private int mAccountId;

    private static final SimpleDateFormat YYYYM_FORMAT = new SimpleDateFormat("yyyy.MM");

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
        //캘린더 header format 변경하기
        widget.setTitleFormatter(new DateFormatTitleFormatter(YYYYM_FORMAT));

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


        getEventList();
    //    result = {"2018,03,18","2018,04,18","2018,05,18","2018,06,18"}; //일정이 들어가는 배열


    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        mEvents.clear(); //리사이클러 뷰에 있는 데이터 지우기
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

        String shot_Day = Year + "." + Month + "." + Day;

        Log.i("shot_Day test", shot_Day + "");

        widget.addDecorator(new MySelectorDecorator(CalendarActivity.this));

        TextView selectDay = (TextView) findViewById(R.id.select_day_tv);
        selectDay.setText(shot_Day);

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

    private  void getEventList(){
        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
        final Call<ArrayList<String>> EventList = NetRetrofit.getInstance().getService().getDatesHavingEvent(mAccountId);

        EventList.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
               // controlJsonObj(response);
                //Log.i("EventList값", EventList.toString());
                //result=(string[])EventList.ToArray(typeof(string));
               // List<String> names = new ArrayList<String>();
                //result = names.toArray(new String[names.size()]);
                ArrayList<String> body = response.body();
                result= body.toArray(new String[body.size()]);
                Log.i("EventList값", body.toString());
                new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
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
    //    Log.i("mYear값", mYear);
    //    Log.i("mMonth값", mMonth);
    //    Log.i("mDate값", mDate);

        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance().getService().getEvent(mAccountId,mYear,mMonth,mDate);

        if(res==null)
            Log.i("null값", "res값이 null");
        else{
            Log.i("null값", "res값이 null 아님ㅁ");
        }

        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                if(response==null)
                    Log.i("null값", "response값이 null");
                else{
                    Log.i("null값", response.toString()+"response값이 null 아님ㅁ");
                }
                controlJsonObj(response);
                mRvEventList.scrollToPosition(mEventAdapter.getItemCount() - 1);
                //mIsLoad = true;
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                if(t!=null){
                    Log.e(TAG, t.toString());
                }
            }
        });
    }

    private void controlJsonObj(Response<ArrayList<JsonObject>> response){

        ArrayList<JsonObject> body = response.body();

        for(JsonObject json : body){
            //정보 가져오기
            mEvents.add(new Event(json.get("id").getAsInt(),json.get("review").getAsString(), json.get("schedule_what").getAsString(), json.get("schedule_when").getAsString(), json.get("schedule_where").getAsString(),-1)); //이미지 일단 넣어둠
        }
        if(body.size() == 0 ){
            // 더이상의 대화 내역이 없음
            //mMoreChat = false;
        }else{
            // 마지막 인덱스 저장
            // CustomSharedPreference.getInstance(getApplicationContext(), "chat").savePreferences("last_index", (body.get(body.size()-1)).get("id").getAsInt());
            mEventAdapter.notifyDataSetChanged();
        }
        //hideProgressBar();

    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }

            Log.i("dates값",dates.toString());
            //widget.addDecorator(new EventDecorator(R.color.colorAccent, dates,CalendarActivity.this));
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            for(CalendarDay day : calendarDays){
                Log.e(TAG, "HERE"+day);
            }
           widget.addDecorator(new EventDecorator(R.color.colorAccent, calendarDays,CalendarActivity.this));
        }
    }
}
