package com.dajeong.chatbot.dajeongbot.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    // 싱글톤 패턴 (singleton)
        // 인스턴스가 사용될 때에 똑같은 인스턴스를 만들어 내는 것이 아니라,
        // 동일 인스턴스를 사용하게끔 하는 것이 기본 전략
        // private 으로 선언된 static 변수와 생성자를 통해
        // public static 으로 선언된 get 함수에서만 접근할 수 있도록 제한

        // lazy initialization을 통해 인스턴스가 사용되는 시점에 생성할 수 있도록
        // https://blog.seotory.com/post/2016/03/java-singleton-pattern
    private static NetRetrofit instance;
    public static NetRetrofit getInstance() {
        if( instance == null){
            instance = new NetRetrofit();
        }
        return instance;
    }
    private NetRetrofit() {
    }

    Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            .baseUrl("https://api.github.com/")
            .build();

    RetrofitService service = retrofit.create(RetrofitService.class);

    public RetrofitService getService() {
        return service;
    }

}
