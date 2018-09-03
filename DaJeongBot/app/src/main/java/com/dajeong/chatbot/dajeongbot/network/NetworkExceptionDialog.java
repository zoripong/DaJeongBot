package com.dajeong.chatbot.dajeongbot.network;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.activity.SplashActivity;

public class NetworkExceptionDialog {
    private Context mContext;

    public NetworkExceptionDialog(Context mContext){
        this.mContext = mContext;
    }


    public void callFunction(String text) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog 클래스를 생성한다.
        final Dialog dlg = new Dialog(mContext);

        // 액티비티의 타이틀바를 숨긴다.
//        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_network_exception);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        TextView textView = dlg.findViewById(R.id.tv_guide_message);

        dlg.findViewById(R.id.btnRetry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "재시도합니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
                ((SplashActivity)mContext).checkInternetStatus();
            }
        });
        dlg.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "취소 했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
