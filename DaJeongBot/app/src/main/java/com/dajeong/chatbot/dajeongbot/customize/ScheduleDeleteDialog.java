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
import com.dajeong.chatbot.dajeongbot.adapter.EventAdapter;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ScheduleDeleteDialog extends Dialog implements android.view.View.OnClickListener{
    private final String TAG = "LogoutDialog";
    ScheduleDeleteDialog dlg;
    int check;
    public ScheduleDeleteDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_logout);
        dlg=this;

        // 액티비티의 타이틀바를 숨긴다.
        //dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

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

        TextView okButton = (TextView) dlg.findViewById(R.id.tvOk);
        TextView cancelButton = (TextView) dlg.findViewById(R.id.tvCancle);

        okButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tvOk:
                Log.e("확인버튼 누름","누름");
                check=0;
                setCheckNum(check);
                dlg.dismiss();
                break;
            case R.id.tvCancle:
                Log.e("취소버튼 누름","누름");
                check=1;
                setCheckNum(check);
                dlg.dismiss();
                break;
        }

    }

    public void setCheckNum(int check){
        this.check=check;
    }
    public int getCheck(){
        return check;
    }
}
