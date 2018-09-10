package com.dajeong.chatbot.dajeongbot.network;

import android.content.Context;
import android.util.Log;

import com.dajeong.chatbot.dajeongbot.control.Util;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetRetrofit {
    private final String TAG = "NetRetrofit";
    // 싱글톤 패턴 (singleton)
        // 인스턴스가 사용될 때에 똑같은 인스턴스를 만들어 내는 것이 아니라,
        // 동일 인스턴스를 사용하게끔 하는 것이 기본 전략
        // private 으로 선언된 static 변수와 생성자를 통해
        // public static 으로 선언된 get 함수에서만 접근할 수 있도록 제한

        // lazy initialization 을 통해 인스턴스가 사용되는 시점에 생성할 수 있도록
        // https://blog.seotory.com/post/2016/03/java-singleton-pattern
    private static NetRetrofit instance;
    private RetrofitService mService;
    public static NetRetrofit getInstance(Context context) {
        if( instance == null){
            instance = new NetRetrofit(context);
        }
        return instance;
    }

    private NetRetrofit(){}

    private NetRetrofit(Context context) {
        Log.e(TAG, Util.getProperty("server_host",context));

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                .baseUrl(Util.getProperty("server_host",context))
                .client(builder.build())
                .build();

        mService = retrofit.create(RetrofitService.class);

    }


    OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            // 헤더를 자유 자재로 변경
            Request.Builder builder = original.newBuilder();
            builder.removeHeader("Content-Type");
            builder.addHeader("Content-Type","application/json; charset=utf-8");
            builder.addHeader("Accept","application/json; charset=utf-8");

            builder.method(original.method(),original.body());
            Request request = builder.build();

            Response response = chain.proceed(request);

            // 아래 소스는 response로 오는 데이터가 URLEncode 되어 있을 때
            // URLDecode 하는 소스 입니다.
            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType()
                            , URLDecoder.decode(response.body().string(),"utf-8")))
                    .build();
        }
    });

    public RetrofitService getService() {
        return mService;
    }

}
