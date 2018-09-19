package com.dajeong.chatbot.dajeongbot.customize;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WhenQuestionDialog extends MainActivity implements View.OnClickListener{
    Dialog dialog;
    EditText messageY;
    EditText messageM;
    EditText messageD;
    String inputMessage;
    public WhenQuestionDialog() {
        dialog = new Dialog(mContext, R.style.DialogTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.main_auto_bottom);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();

        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.ivWhenCloseBtn);
        messageY = (EditText) dialog.findViewById(R.id.etYear);
        messageM = (EditText) dialog.findViewById(R.id.etMonth);
        messageD = (EditText) dialog.findViewById(R.id.etDay);
        ImageView btnSend = (ImageView) dialog.findViewById(R.id.btnWhenSend);

        closeBtn.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivWhenCloseBtn:
                dialog.dismiss();
                break;
            case R.id.btnWhenSend:
                if (isNumeric(messageY.getText().toString()) && isNumeric(messageM.getText().toString()) && isNumeric(messageD.getText().toString())) {
                    int inputY = Integer.parseInt(messageY.getText().toString());
                    StringBuilder inputM = new StringBuilder(messageM.getText().toString());
                    if (messageM.getText().toString().charAt(0) == '0') {
                        inputM.deleteCharAt(0);
                    }
                    StringBuilder inputD = new StringBuilder(messageD.getText().toString());
                    if (messageD.getText().toString().charAt(0) == '0') {
                        inputD.deleteCharAt(0);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c1 = Calendar.getInstance();
                    String strToday = sdf.format(c1.getTime());

                    String[] dateArray = strToday.split("-");
                    String mYear = dateArray[0];
                    String mMonth = dateArray[1];
                    String mDate = dateArray[2];
                    if (inputY <= Integer.parseInt(mYear) && inputY > 2000 &&
                            Integer.parseInt(inputM.toString()) <= Integer.parseInt(mMonth) && Integer.parseInt(inputM.toString()) >= 1 &&
                            Integer.parseInt(inputD.toString()) < Integer.parseInt(mDate) && Integer.parseInt(inputD.toString()) >= 1) {
                        inputMessage = messageY.getText().toString() + "년 " + messageM.getText().toString() + "월 " + messageD.getText().toString() + "일이야!";
                        ((MainActivity)mContext).clickSendMessage(inputMessage);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(mContext, "지나간 날짜를 입력해주세요!", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(mContext, "올바른 날짜를 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                    break;

        }
    }
    public static boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }
}