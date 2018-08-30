package com.dajeong.chatbot.dajeongbot.control;

import android.content.Context;
import android.content.SharedPreferences;

public class CustomSharedPreference {
    private SharedPreferences pref;
    private static CustomSharedPreference instance;

    public static CustomSharedPreference getInstance(Context context, String prefName) {
        if( instance == null){
            instance = new CustomSharedPreference(context, prefName);
        }
        return instance;
    }

    private CustomSharedPreference(Context context, String prefName) {
        pref = context.getSharedPreferences(prefName, context.MODE_PRIVATE);
    }

    //데이터 가져오기
    public String getStringPreferences(String key){
        return pref.getString(key, ""); //key, value(defaults)
    }

    public int getIntPreferences(String key){
        return pref.getInt(key, -1); //key, value(defaults)
    }

    //데이터 저장하기
    public void savePreferences(String key, String value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void savePreferences(String key, int value){
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
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
