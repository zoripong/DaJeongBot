package com.dajeong.chatbot.dajeongbot.customize;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;

public class WhereDialog extends MainActivity implements View.OnClickListener{
    Dialog dialog;
    EditText message;
    String inputMessage;
    public WhereDialog() {
        dialog = new Dialog(mContext, R.style.DialogTheme);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_where);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.show();

        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.ivWhereCloseBtn);
        message = (EditText) dialog.findViewById(R.id.etWhereMessage);
        ImageView btnSend = (ImageView) dialog.findViewById(R.id.btnWhereSend);

        closeBtn.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivWhereCloseBtn :
                dialog.dismiss();
                break;
            case R.id.btnWhereSend :
                inputMessage=message.getText().toString()+"에서 하는거야!";
                if (message.getText() != null && !message.getText().toString().replace(" ", "").equals("")) {

                    ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                    dialog.dismiss();
                    new WhenDialog();
                }
                else {
                    Toast.makeText(getApplicationContext(), "메시지를 입력해주세요!", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}