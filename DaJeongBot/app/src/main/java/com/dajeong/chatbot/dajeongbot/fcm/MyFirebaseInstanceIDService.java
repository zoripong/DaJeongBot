package com.dajeong.chatbot.dajeongbot.fcm;

import android.util.Log;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestRegisterToken;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateToken;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "token updating ...  : "+token);
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");
        String fcmToken = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("fcm_token");

        // 회원정보가 있을 경우
        if(accountId != null && !accountId.equals("")){
            final RequestUpdateToken param = new RequestUpdateToken(Integer.parseInt(accountId), fcmToken, token);
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().updateFcmToken(param);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.body().has("status")){
                        if(response.body().get("status").getAsString().equals("Success")){
                            Log.i(TAG, "토큰 업데이트에 성공하였습니다.\n"+ param.toString());
                            CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("fcm_token", token);

                        }else{
                            Log.e(TAG, "서버의 문제로 토큰 등록에 실패하였습니다.");
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if(t!=null){
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 해당 기기 토큰을 저장하지 못하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}
