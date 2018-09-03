package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.fcm.MyFirebaseInstanceIDService;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.network.NetworkExceptionDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// intro activity
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        test();
//        checkInternetStatus();

        startApp();
    }

    public void checkInternetStatus() {

        Call<String> res = NetRetrofit
                .getInstance(getApplicationContext())
                .getService()
                .checkInternetStatus();

        final String[] message = {"서버에 문제가 발생하였습니다. 서비스 이용에 불편을 드려 대단히 죄송합니다. 빠른 시일 내에 원활한 서비스 이용이 가능하도록 하겠습니다."};
        final NetworkExceptionDialog networkExceptionDialog = new NetworkExceptionDialog(SplashActivity.this);

        res.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response!=null){
                    if(!(response.body()!=null && "hello world".equals(response.body()))){
                        // 실패
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                networkExceptionDialog.callFunction(message[0]);
                            }
                        });
                    }else{
                        startApp();

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    message[0] = "네트워크 연결을 확인해주세요. 다정봇은 인터넷이 필요한 서비스입니다.";
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkExceptionDialog.callFunction(message[0]);
                    }
                });
            }
        });
    }

    public void test(){
//        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("id", "32");
//        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("bot_type", 0);
        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").removeAllPreferences();


        Log.e(TAG, "firebase device token is :" + FirebaseInstanceId.getInstance().getToken());

    }

    public void startApp(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id").equals("")){
                    // 로그인 내역이 남아있다면
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    // 처음일 경우 tutorial, 아닐 경우 LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
