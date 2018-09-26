package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.dajeong.chatbot.dajeongbot.R;
import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;

public class ImageDetailActivity extends AppCompatActivity {
    private final String TAG = "ImageDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

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
    }
}
