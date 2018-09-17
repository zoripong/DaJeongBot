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

public class WhenDialog extends MainActivity implements View.OnClickListener{
    Dialog dialog;
    EditText messageY;
    EditText messageM;
    EditText messageD;
    String inputMessage;
    public WhenDialog() {
        dialog = new Dialog(mContext, R.style.DialogTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_when);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setDimAmount(0.0f);
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
        switch (view.getId()){
            case R.id.ivWhenCloseBtn :
                dialog.dismiss();
                break;
            case R.id.btnWhenSend :
                inputMessage=messageY.getText().toString()+"년 "+messageM.getText().toString()+"월 "+messageD.getText().toString()+"일이야!";
                if (messageY.getText() != null && !messageY.getText().toString().replace(" ", "").equals("")&&messageM.getText() != null && !messageM.getText().toString().replace(" ", "").equals("")&&
                        messageD.getText() != null && !messageD.getText().toString().replace(" ", "").equals("")) {
                    ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                    dialog.dismiss();
                    new WhatDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "날짜를 입력해주세요!", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}