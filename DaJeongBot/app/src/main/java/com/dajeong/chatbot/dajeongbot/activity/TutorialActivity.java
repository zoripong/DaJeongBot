package com.dajeong.chatbot.dajeongbot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.R;
import com.rd.PageIndicatorView;

public class TutorialActivity extends Activity {
    int[] mResources = {
            R.drawable.tutorial_01,
            R.drawable.tutorial_02,
            R.drawable.tutorial_03,
            R.drawable.tutorial_04,
            R.drawable.tutorial_05,
            R.drawable.tutorial_06
    };
    RelativeLayout lastTutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);
        lastTutorial=(RelativeLayout) findViewById(R.id.endTutoral);
        //액티비티 크기 변경
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();

        float width = (int)(dm.widthPixels*0.8f);
        Log.e("Tutorial width: ",Float.toString(width));
        float a=1156/width;
        int b=(int)(1605/a);
        Log.e("Tutorial height: ",Float.toString(a));
        Log.e("Tutorial height: ",Integer.toString(b));
        getWindow().getAttributes().width = (int)width;
        getWindow().getAttributes().height = b;

        //this.setFinishOnTouchOutside(false);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL , WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        //ssgetWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);





        final CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mCustomPagerAdapter);

        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(6); // specify total count of indicators
        //pageIndicatorView.setSelection(2);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int cur = viewPager.getCurrentItem();    //현재 아이템 포지션
                if(cur ==5)        //마지막 페이지가 아니면
                    lastTutorial.setVisibility(View.VISIBLE);
                else                        //마지막 페이지 이면
                    lastTutorial.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.item_tutorial, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//            imageView.setImageResource(mResources[position]);

            Glide.with(TutorialActivity.this)
                    .load(mResources[position])
                    .into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}

