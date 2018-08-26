package com.dajeong.chatbot.dajeongbot.activity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.adapter.EventAdapter;
import com.dajeong.chatbot.dajeongbot.decorators.EventDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.OneDayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SaturdayDecorator;
import com.dajeong.chatbot.dajeongbot.decorators.SundayDecorator;
import com.dajeong.chatbot.dajeongbot.model.Event;
import com.dajeong.chatbot.dajeongbot.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

// 캘린더 activity
public class CalendarActivity extends AppCompatActivity {

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;

    // calendar view
    MaterialCalendarView materialCalendarView;

    // recycler view
    //private LinkedList<Event> mEvents;
    private ArrayList<Event> mEvents = new ArrayList<>();
    private RecyclerView mRvEventList;
    private EventAdapter mEventAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(CalendarActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

        mRvEventList = findViewById(R.id.event_root);
        mRvEventList.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRvEventList.setLayoutManager(mLayoutManager);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2018,03,18","2018,04,18","2018,05,18","2018,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int Year = date.getYear();
                int Month = date.getMonth() + 1;
                int Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                String shot_Day = Year + "." + Month + "." + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                TextView selectDay = (TextView) findViewById(R.id.select_day_tv);
                selectDay.setText(shot_Day);

                ////제목만 있는 경우도 하기
                forDebuggingEvent();
                mEventAdapter = new EventAdapter(mEvents);

                mRvEventList.setAdapter(mEventAdapter);
            }
        });
    }

    private void forDebuggingEvent(){


        mEvents.add(new Event("수진이와 롯데월드", "교복을 입고 갔는데 너무 불편했어. 그래도 생각보다 사람이 적어서 다행이야!",R.drawable.applozic_audio_delete));
        mEvents.add(new Event("수진이와 롯데월드", "교복을 입고 갔는데 너무 불편했어. 그래도 생각보다 사람이 적어서 다행이야!",-1)); //이미지가 없는 경우 -1로 처리
        mEvents.add(new Event("수학숙제 끝내기",null,-1));



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
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new EventDecorator(R.color.colorAccent, calendarDays,CalendarActivity.this));
        }
    }
}