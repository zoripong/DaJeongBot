package com.dajeong.chatbot.dajeongbot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.activity.AddPhotoActivity;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.Chat;

import java.util.LinkedList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AutoDialog extends MainActivity implements View.OnClickListener{
    Dialog dialog;
    EditText message;
    String inputMessage;
    public AutoDialog(Context context) {

        dialog = new Dialog(context, R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_auto_complete);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow()
                .getAttributes().windowAnimations = R.style.PauseDialogAnimation;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.show();

        ImageView scheduleAdd = (ImageView) dialog.findViewById(R.id.ivScheduleAdd);
        ImageView recall = (ImageView) dialog.findViewById(R.id.ivRecall);
        ImageView closeBtn = (ImageView) dialog.findViewById(R.id.ivCloseBtn);
        message = (EditText) dialog.findViewById(R.id.etDMessage);
        ImageView btnSend = (ImageView) dialog.findViewById(R.id.btnDSend);


        scheduleAdd.setOnClickListener(this);
        recall.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        btnSend.setOnClickListener(this);

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ivScheduleAdd :
                inputMessage="나 일정 있어!";
                ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                ((MainActivity)MainActivity.mContext).addAutoSchedule();
                dialog.dismiss();
                break;
            case R.id.ivRecall :
                inputMessage="나 궁금한 날 있어!";
                ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                ((MainActivity)MainActivity.mContext).addAutoRecall();
                dialog.dismiss();
                break;
            case R.id.ivCloseBtn :
                dialog.dismiss();
                break;
            case R.id.btnDSend :
                inputMessage=message.getText().toString();
                if (message.getText() != null && !message.getText().toString().replace(" ", "").equals("")) {
                    ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(), "메시지를 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}