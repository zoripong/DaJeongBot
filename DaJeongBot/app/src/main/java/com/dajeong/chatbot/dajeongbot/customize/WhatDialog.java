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

public class WhatDialog extends MainActivity implements View.OnClickListener{
    Dialog dialog;
    EditText message;
    String inputMessage;
    public WhatDialog() {
        dialog = new Dialog(mContext, R.style.DialogTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_what);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        dialog.show();

        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.ivWhatCloseBtn);
        message = (EditText) dialog.findViewById(R.id.etWhatMessage);
        ImageView btnSend = (ImageView) dialog.findViewById(R.id.btnWhatSend);

        closeBtn.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivWhatCloseBtn :
                dialog.dismiss();
                break;
            case R.id.btnWhatSend :
                inputMessage=message.getText().toString()+"을(를) 하는거야";
                if (message.getText() != null && !message.getText().toString().replace(" ", "").equals("")) {
                    ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(), "무엇을 하는지를 입력해주세요!", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}