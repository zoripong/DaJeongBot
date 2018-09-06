package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import com.dajeong.chatbot.dajeongbot.alias.AccountType;
import com.dajeong.chatbot.dajeongbot.fragment.InputNameFragment;
import com.dajeong.chatbot.dajeongbot.fragment.IntroduceFragment;
import com.dajeong.chatbot.dajeongbot.fragment.SelectCharacterFragment;
import com.dajeong.chatbot.dajeongbot.fragment.SignUpFragment;
import com.dajeong.chatbot.dajeongbot.R;

// 사용자 정보 입력 activity
public class SignupActivity extends AppCompatActivity{
    //TODO : back 버튼 안먹히게
    /*
     * 0 : basic
     * 1 : facebook
     * 2 : kakao
     * 3 : google 
     * */
    private final String TAG = "SignupActivity";
    private final int NUM_PAGES = 5;

    private int mAccountType = -1;
    private String mToken = "";

    private ViewPager mPager;
    protected ViewPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_input_info);

        Intent intent = getIntent();
        mAccountType = intent.getIntExtra("account_type", -1);
        mToken = intent.getStringExtra("token");


        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        Log.e(TAG, String.valueOf(mAccountType));
        if(mAccountType > AccountType.BASIC_ACCOUNT){
            setCurrentItem(2, true);
        }




//        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this, SelectBotActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }
    public void setCurrentItem(int item, boolean smoothScroll){
        mPager.setCurrentItem(item, smoothScroll);
    }
    public int getCurrentItem(){
        return mPager.getCurrentItem();
    }
    @Override
    public void onBackPressed(){
        if(mPager.getCurrentItem()==0){
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public int getAccountType(){
        return mAccountType;
    }

    public String getToken() {
        if(mToken != null)
           return mToken;
        else return "";
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter{
        public ViewPagerAdapter(FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new SignUpFragment();
                    break;
                case 1:
                    fragment = new InputNameFragment();
                    break;
                case 2:
                    fragment = new IntroduceFragment();
                    break;
                case 3:
                    fragment = new SelectCharacterFragment();
                    break;
                default:
                    fragment = new Fragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
