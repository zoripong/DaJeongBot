package com.dajeong.chatbot.dajeongbot.customize;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.LoginActivity;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.activity.SettingActivity;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestRegisterToken;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ResetDialog {
    private final String TAG = "LogoutDialog";
    private final String INTENT_RESET = "INTENT_RESET";

    private Context context;

    public ResetDialog(Context context) {
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_logout);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = (int)(dm.widthPixels*0.8f);
        WindowManager.LayoutParams wm = dlg.getWindow().getAttributes();
        wm.copyFrom(dlg.getWindow().getAttributes());
        wm.width = width;
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dlg.getWindow()
                .getAttributes().windowAnimations = R.style.AlphaDialogAnimation;
        dlg.show();

        final TextView dialogText = (TextView) dlg.findViewById(R.id.dialogText);
        dialogText.setText("확인버튼을 누를 경우, 누적되었던 채팅 내역과 일정에 관한 데이터가 전부 사라집니다.\n사라진 데이터에 대해서는 다시 되돌릴 수 없습니다.");
        final TextView okButton = (TextView) dlg.findViewById(R.id.tvOk);
        final TextView cancelButton = (TextView) dlg.findViewById(R.id.tvCancle);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetData(dlg);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
    public void resetData(final Dialog dlg){
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");

        Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().resetData(Integer.parseInt(accountId));
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().has("status")) {
                    if (response.body().get("status").getAsString().equals("Success")) {
                        Toast.makeText(getApplicationContext(), "성공적으로 데이터를 초기화했습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.putExtra(INTENT_RESET, 0);
                        ((Activity)context).setResult(RESULT_OK,intent);

                        dlg.dismiss();
                        ((Activity)context).finish();


                    } else {
                        Log.e(TAG, "서버의 문제로 일정 삭제에 실패하였습니다.");
                        dlg.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if (t != null) {
                    Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 일정을 삭제하지 못하였습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
