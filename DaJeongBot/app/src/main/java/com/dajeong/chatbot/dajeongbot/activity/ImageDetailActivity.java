package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;

public class ImageDetailActivity extends AppCompatActivity {
    private final String TAG = "ImageDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image_detail);

        findViewById(R.id.ivClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String filePath = intent.getStringExtra("IMAGE_SRC");
        String sendTime = intent.getStringExtra("SEND_TIME");

        Log.e(TAG, "filePath : "+ filePath);
        Log.e(TAG, "sendTime : "+ sendTime);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget((ImageView)findViewById(R.id.ivDetail));
        Glide.with(this)
                .load(filePath)
                .thumbnail(Glide.with(this).load(R.raw.img_loading))
                .into(imageViewTarget);

        ((TextView)findViewById(R.id.tvTime)).setText(sendTime);

        findViewById(R.id.ivSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "잠시만 기다려주세요! 저장을 위해 준비중이랍니다 :)", Toast.LENGTH_LONG).show();
            }
        });
    }
}
