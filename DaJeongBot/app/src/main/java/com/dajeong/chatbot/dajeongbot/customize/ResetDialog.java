package com.dajeong.chatbot.dajeongbot.customize;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.SettingActivity;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ResetDialog {
    private final String TAG = "LogoutDialog";
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
        dialogText.setText("데이터를 초기화하시겠습니까?");
        final TextView okButton = (TextView) dlg.findViewById(R.id.tvOk);
        final TextView cancelButton = (TextView) dlg.findViewById(R.id.tvCancle);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetData();
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
    public void resetData(){
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();

    }
}
