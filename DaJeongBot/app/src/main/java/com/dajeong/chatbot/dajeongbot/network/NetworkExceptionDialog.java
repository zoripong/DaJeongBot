package com.dajeong.chatbot.dajeongbot.network;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = (int)(dm.widthPixels*0.8f); //디바이스 화면 너비
        //int height = dm.heightPixels; //디바이스 화면 높이

        WindowManager.LayoutParams wm = dlg.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(dlg.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width;  //화면 너비의 절반
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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
//        dlg.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "취소 했습니다.", Toast.LENGTH_SHORT).show();
//
//                // 커스텀 다이얼로그를 종료한다.
//                dlg.dismiss();
//            }
//        });
    }
}
