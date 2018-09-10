package com.dajeong.chatbot.dajeongbot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.R;
import com.rd.PageIndicatorView;

public class TutorialActivity extends Activity {
    int[] mResources = {
            R.drawable.tutorial_01,
            R.drawable.tutorial_02,
            R.drawable.tutorial_03,
            R.drawable.tutorial_04,
            R.drawable.tutorial_05
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutorial);

        //액티비티 크기 변경
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().getAttributes().width = 1156;
        getWindow().getAttributes().height = 1605;

        //this.setFinishOnTouchOutside(false);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL , WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        //ssgetWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);





        final CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(this);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(mCustomPagerAdapter);

        final PageIndicatorView pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setCount(5); // specify total count of indicators
        //pageIndicatorView.setSelection(2);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
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

