package com.dajeong.chatbot.dajeongbot.customize;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by s2017 on 2018-08-04.
 */

public class SwipeViewPager extends ViewPager {

    private boolean enabled;

    public SwipeViewPager(Context context){super(context);}

    public SwipeViewPager(Context context, AttributeSet attrs){super(context, attrs);}

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        if (enabled){
            return super.onInterceptTouchEvent(ev);
        } else {
            if (MotionEventCompat.getActionMasked(ev)==MotionEvent.ACTION_MOVE) {
            } else {
                if (super.onInterceptTouchEvent(ev)){
                    super.onTouchEvent(ev);
                }
            }
            return false;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        if(enabled){
            return super.onTouchEvent(ev);
        } else {
            return MotionEventCompat.getActionMasked(ev) != MotionEvent.ACTION_MOVE && super.onTouchEvent(ev);
        }
    }
    public void setPaingEnabled(boolean enabled){
        this.enabled = enabled;
    }
}
