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
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdatePassword;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

public class PasswordDialog {
    private final String TAG = "PasswordDialog";

    private Context context;
    private String mPassword;
    public PasswordDialog(Context context, String password) {
        this.context = context;
        this.mPassword = password;
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

        dlg.getWindow().getAttributes().windowAnimations = R.style.AlphaDialogAnimation;
        dlg.show();

        final TextView dialogText = (TextView) dlg.findViewById(R.id.dialogText);
        dialogText.setText("비밀번호를 수정하시겠습니까?");
        final TextView okButton = (TextView) dlg.findViewById(R.id.tvOk);
        final TextView cancelButton = (TextView) dlg.findViewById(R.id.tvCancle);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword(dlg, mPassword);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
    private void changePassword(final Dialog dlg, String password) {
        String accountId = CustomSharedPreference
                .getInstance(getApplicationContext(), "user_info")
                .getStringPreferences("id");
        Log.e(TAG, "잉"+accountId);
        if (!(accountId == null && accountId.equals(""))) {
            final RequestUpdatePassword param = new RequestUpdatePassword(Integer.parseInt(accountId), password);
            Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().updatePassword(param);
            res.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body().has("status")) {
                        if (response.body().get("status").getAsString().equals("Success")) {
                            Log.e(TAG, "비밀번호 변경 성공");
                            Toast.makeText(getApplicationContext(), "성공적으로 비밀번호를 변경했습니다", Toast.LENGTH_LONG).show();
                            dlg.dismiss();
                            ((Activity)context).finish();
                        } else {
                            Log.e(TAG, "서버의 문제로 이름 변경에 실패하였습니다.");
                            Toast.makeText(getApplicationContext(), "서버에 문제가 발생하여 이름을 변경하지 못하였습니다. 개발자에게 문의를 남겨주세요.", Toast.LENGTH_LONG).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (t != null) {
                        Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 이름을 변경하지 못하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }
}
