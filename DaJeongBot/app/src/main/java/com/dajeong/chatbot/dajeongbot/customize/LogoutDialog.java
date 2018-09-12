package com.dajeong.chatbot.dajeongbot.customize;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.LoginActivity;
import com.dajeong.chatbot.dajeongbot.activity.SettingActivity;
import com.dajeong.chatbot.dajeongbot.activity.SplashActivity;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LogoutDialog {
    private final String TAG = "LogoutDialog";
    private Context context;

    public LogoutDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_logout);

        // 커스텀 다이얼로그를 노출한다.
        DisplayMetrics dm = context.getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = (int)(dm.widthPixels*0.8f); //디바이스 화면 너비
        //int height = dm.heightPixels; //디바이스 화면 높이

        WindowManager.LayoutParams wm = dlg.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(dlg.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width;
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final TextView okButton = (TextView) dlg.findViewById(R.id.tvOk);
        final TextView cancelButton = (TextView) dlg.findViewById(R.id.tvCancle);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                logout();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
    public void logout(){
        final String accountId = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id");
        final String token = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("fcm_token");
        Log.e(TAG, "account ID : "+accountId);
        Log.e(TAG, "token: "+token);
        if(accountId!=null && !token.equals("")){
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().releaseFcmToken(Integer.parseInt(accountId), token);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.body().has("status")){
                        if(response.body().get("status").getAsString().equals("Success")){
                            Log.i(TAG, "토큰 해제 성공하였습니다."+ accountId);
                            ((SettingActivity)context).releaseUserInfo();
                        }else{
                            Log.e(TAG, "서버의 문제로 토큰 등록에 실패하였습니다.");
                            Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 로그아웃에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if(t!=null){
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 로그아웃에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            ((SettingActivity)context).releaseUserInfo();
        }

    }
}
