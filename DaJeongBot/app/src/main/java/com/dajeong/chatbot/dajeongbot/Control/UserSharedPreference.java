package com.dajeong.chatbot.dajeongbot.Control;

import android.content.Context;
import android.content.SharedPreferences;

import com.dajeong.chatbot.dajeongbot.Network.NetRetrofit;

public class UserSharedPreference {
    private SharedPreferences pref;
    private static UserSharedPreference instance;

    public static UserSharedPreference getInstance(Context context, String prefName) {
        if( instance == null){
            instance = new UserSharedPreference(context, prefName);
        }
        return instance;
    }

    private UserSharedPreference(Context context, String prefName) {
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
    }

    //데이터 가져오기
    public String getPreferences(String key){
        return pref.getString(key, ""); //key, value(defaults)
    }

    //데이터 저장하기
    public void savePreferences(String key, String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //데이터 삭제
    public void removePreferences(String key){
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }

    //모든 데이터 삭제
    public void removeAllPreferences(){
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
