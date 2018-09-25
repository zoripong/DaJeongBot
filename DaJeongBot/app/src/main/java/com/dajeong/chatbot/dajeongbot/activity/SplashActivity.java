package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

// intro activity
public class SplashActivity extends AppCompatActivity {
    private final String TAG = "SplashActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 액션바 없애기
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_splash);
        getHashKey();
        test();
        checkInternetStatus();
    }


    private void test(){
        Log.e(TAG, "firebase device token is :" + FirebaseInstanceId.getInstance().getToken());
    }

    private void startApp(){
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

    public void checkInternetStatus() {
        Call<JsonObject> res = NetRetrofit
                .getInstance(getApplicationContext())
                .getService()
                .checkInternetStatus();

        final String[] message = {"서버에 문제가 발생하였습니다.\n \n 서비스 이용에 불편을 드려 대단히 죄송합니다.\n빠른 시일 내에 원활한 서비스 이용이\n가능하도록 하겠습니다."};
        final NetworkExceptionDialog networkExceptionDialog = new NetworkExceptionDialog(SplashActivity.this);

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, response.toString());
                if(response!=null){
                    if(!(response.body()!=null && "Success".equals(response.body().get("status").getAsString()))){
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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, t.toString());
                if(t instanceof SocketTimeoutException){
                    message[0] = "네트워크 연결을 확인해주세요.\n 다정봇은 인터넷이 필요한 서비스입니다.";
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

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }
}
