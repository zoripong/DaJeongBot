package com.dajeong.chatbot.dajeongbot.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.Activity.SignupActivity;
import com.dajeong.chatbot.dajeongbot.Alias.AccountType;
import com.dajeong.chatbot.dajeongbot.Control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.Network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by s2017 on 2018-08-03.
 */

public class SelectCharacterFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "SelectCharacterFragment";
    private Button btnSelectDone;
    private Button btnSelectNext, btnSelectPrevious;

    private int count = 0;
    private int selectImage[];
    private int selectDot[];

    private int introduceHead[];
    private int introduceTitle[];
    private int introduceString[];
    private int introduceImage[];

    private ImageView mIvBotHead, mIvBotTitle, mIvBotIntroduce;
    private TextView mTvBotString;
    private LinearLayout mLiBtnPrevious, mLiBtnNext;
//    private Button mBtnBotImg01,mBtnBotImg02,mBtnBotImg03,mBtnBotImg04;
//    private ImageView mIvBotDot01, mIvBotDot02, mIvBotDot03, mIvBotDot04;


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_select_bot, container, false);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.view_select_bot, container, false);
        selectImage = new int[]{R.id.img_chatbot_1, R.id.img_chatbot_2, R.id.img_chatbot_3, R.id.img_chatbot_4};
        selectDot = new int[]{R.id.img_select_dot_1, R.id.img_select_dot_2, R.id.img_select_dot_3, R.id.img_select_dot_4};

        introduceHead = new int[]{R.drawable.chatbot_head_1, R.drawable.chatbot_head_2, R.drawable.chatbot_head_3, R.drawable.chatbot_head_4};
        introduceTitle = new int[]{R.drawable.chatbot_text_1, R.drawable.chatbot_text_2, R.drawable.chatbot_text_3, R.drawable.chatbot_text_4};
        introduceString = new int[]{R.string.dajeong_1_info, R.string.dajeong_2_info, R.string.dajeong_3_info, R.string.dajeong_4_info};
        introduceImage = new int[]{R.drawable.chatbot_introduce_1, R.drawable.chatbot_introduce_2, R.drawable.chatbot_introduce_3,R.drawable.chatbot_introduce_4};

        mIvBotHead = rootView.findViewById(R.id.img_head);
        mIvBotTitle = rootView.findViewById(R.id.img_title);
        mTvBotString = rootView.findViewById(R.id.text_string);
        mIvBotIntroduce = rootView.findViewById(R.id.img_introduce);
        mLiBtnPrevious = rootView.findViewById(R.id.layout_btn_previous);
        mLiBtnNext = rootView.findViewById(R.id.layout_btn_next);

//        Glide.with(this).load(R.raw.chatbot_move_image_4).into(mIvBotIntroduce);

        btnSelectPrevious = rootView.findViewById(R.id.btn_select_previous);
        btnSelectNext = rootView.findViewById(R.id.btn_select_next);

        final ArrayList<Button> botHeads = new ArrayList<>();
        ArrayList<ImageView> botDots = new ArrayList<>();
        ArrayList<ImageView> botIntroduceHeads = new ArrayList<>();
        ArrayList<ImageView> botIntroduceTitles = new ArrayList<>();
        ArrayList<String> botIntroduceStrings = new ArrayList<>();
        ArrayList<ImageView> botIntroduceImages = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            botHeads.add((Button) rootView.findViewById(R.id.img_chatbot_1 + i));
            botDots.add((ImageView) rootView.findViewById(R.id.img_select_dot_1 + i));
            botIntroduceHeads.add((ImageView) rootView.findViewById(R.drawable.chatbot_head_1 + i));
            botIntroduceTitles.add((ImageView) rootView.findViewById(R.drawable.chatbot_text_1 + i));
            botIntroduceStrings.add(String.valueOf(R.string.dajeong_1_info + i));
            botIntroduceImages.add((ImageView) rootView.findViewById(R.drawable.chatbot_introduce_1 + i));
        }

        for (int i = 0; i < 4; i++) {
            botHeads.set(i, (Button) rootView.findViewById(selectImage[i]));
        }

        for (int i = 0; i < botHeads.size(); i++) {
            botHeads.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button clickButton = (Button) v;
                    int index = botHeads.indexOf(clickButton);

                    if(index==0){
                        btnSelectPrevious.setVisibility(View.INVISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                    } else if(index == 1 || index == 2){
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                    } else if(index == 3){
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.INVISIBLE);
                    }
                    changeVisibleImage(index);
                    changeIntroduceImage(index);
                }
            });
        }

        btnSelectDone = rootView.findViewById(R.id.btn_select);
        btnSelectDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 필요한 데이터 ( user_id, name, birthday, account_type, bot_type )
                //TODO : 앞의 프래그먼트에서 전부 가져오기
                //TODO : API 로 로그인시 첫 유저이면 SIGN ACTIVITY 로
                // 첫 유저일 경우 http://172.30.1.35/apis/users/{user_id}/{password}로 쿼리시 [{"status": "Failed"}] 반환
                //TODO : TOKEN 저장
                String userId = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_email");
                String name = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_name");
                String birthday = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_year")
                        .concat(CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_month"))
                        .concat(CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_day"));
                int botType = 1;
                int accountType = ((SignupActivity) getActivity()).getAccountType();
                String password = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_pw");
                String token = "";

                // 추가 데이터 ( basic account : password / api account : token )
                switch (accountType) {
                    case AccountType.BASIC_ACCOUNT:
                        password = "test";
                        break;
                    case AccountType.FACEBOOK_ACCOUNT:
                    case AccountType.KAKAO_ACCOUNT:
                    case AccountType.GOOGLE_ACCOUNT:
                        token = "token"; //TODO
                        Toast.makeText(getActivity().getApplicationContext(), "준비주ㅇ", Toast.LENGTH_LONG).show();
                        break;

                }

                // prepare call in Retrofit 2.0
                try {
                    JSONObject paramObject = new JSONObject();
                    paramObject.put("user_id", userId);
                    paramObject.put("name", name);
                    paramObject.put("birthday", birthday);
                    paramObject.put("bot_type", botType);
                    paramObject.put("account_type", accountType);
                    paramObject.put("password", password);
                    paramObject.put("token", token);

                    Call<JsonObject> res = NetRetrofit.getInstance().getService().addUserInfo(paramObject.toString());
                    res.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            Log.e(TAG, String.valueOf(response.body()));
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            if (t != null)
                                Log.e(TAG, t.getMessage());
                        }
                    });

//                    userCall.enqueue(this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        // End


        mLiBtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                switch (count) {
                    case 0:
                        btnSelectPrevious.setVisibility(View.INVISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(0);
                        changeIntroduceImage(0);
                        break;
                    case 1:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(1);
                        changeIntroduceImage(1);
                        break;
                    case 2:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(2);
                        changeIntroduceImage(2);
                        break;
                    case 3:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.INVISIBLE);
                        changeVisibleImage(3);
                        changeIntroduceImage(3);
                        break;
                }
            }
        });
        mLiBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                switch (count) {
                    case 0:
                        btnSelectPrevious.setVisibility(View.INVISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(0);
                        changeIntroduceImage(0);
                        break;
                    case 1:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(1);
                        changeIntroduceImage(1);
                        break;
                    case 2:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        changeVisibleImage(2);
                        changeIntroduceImage(2);
                        break;
                    case 3:
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.INVISIBLE);
                        changeVisibleImage(3);
                        changeIntroduceImage(3);
                        break;
                }

            }
        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_chatbot_1:
                btnSelectPrevious.setVisibility(View.INVISIBLE);
                btnSelectNext.setVisibility(View.VISIBLE);
                changeVisibleImage(0);
                changeIntroduceImage(0);
                break;
            case R.id.img_chatbot_2:
                btnSelectPrevious.setVisibility(View.VISIBLE);
                btnSelectNext.setVisibility(View.VISIBLE);
                changeVisibleImage(1);
                changeIntroduceImage(1);
                break;
            case R.id.img_chatbot_3:
                btnSelectPrevious.setVisibility(View.VISIBLE);
                btnSelectNext.setVisibility(View.VISIBLE);
                changeVisibleImage(2);
                changeIntroduceImage(2);
                break;
            case R.id.img_chatbot_4:
                btnSelectPrevious.setVisibility(View.VISIBLE);
                btnSelectNext.setVisibility(View.INVISIBLE);
                changeVisibleImage(3);
                changeIntroduceImage(3);
                break;
        }
    }

    private void changeVisibleImage(int index) {
        for (int i = 0; i < 4; i++) {
            getView().findViewById(selectDot[i]).setVisibility(View.INVISIBLE);
        }
        getView().findViewById(selectDot[index]).setVisibility(View.VISIBLE);
    }

    private void changeIntroduceImage(int index) {
        mIvBotHead.setImageResource(introduceHead[index]);
        mIvBotTitle.setImageResource(introduceTitle[index]);
        mTvBotString.setText(introduceString[index]);
        mIvBotIntroduce.setImageResource(introduceImage[index]);
    }

    // 서버와 통신하기 위한 내부 클래스 ( 건들이지말아주렴.. ! )
    private class NetworkCall extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<JsonObject> call = calls[0];
                Response<JsonObject> response = call.execute();
                return response.body().toString();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                Log.e(TAG, result);
        }
    }

}

