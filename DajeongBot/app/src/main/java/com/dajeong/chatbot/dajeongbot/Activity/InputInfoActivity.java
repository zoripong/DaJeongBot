package com.dajeong.chatbot.dajeongbot.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.dajeong.chatbot.dajeongbot.Fragment.InputNameFragment;
import com.dajeong.chatbot.dajeongbot.Fragment.IntroduceFragment;
import com.dajeong.chatbot.dajeongbot.Fragment.SelectCharacterFragment;
import com.dajeong.chatbot.dajeongbot.R;

// 사용자 정보 입력 activity
public class InputInfoActivity extends AppCompatActivity
{
    private static final int NUM_PAGES = 3;
    private boolean enabled;
    private ViewPager mPager;
    protected ViewPagerAdapter mPagerAdapter;

    private InputNameFragment inputNameFragment;
    private IntroduceFragment introduceFragment;
    private SelectCharacterFragment selectCharacterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_info);

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(InputInfoActivity.this, SelectBotActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
    public void setCurrentItem(int item, boolean smoothScroll){
        mPager.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void onBackPressed(){
        if(mPager.getCurrentItem()==0){
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    inputNameFragment = new InputNameFragment();
                    return inputNameFragment;
                case 1:
                    introduceFragment = new IntroduceFragment();
                    return introduceFragment;
                case 2:
                    selectCharacterFragment = new SelectCharacterFragment();
                    return selectCharacterFragment;
                default:
                    inputNameFragment = new InputNameFragment();
                    return inputNameFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
