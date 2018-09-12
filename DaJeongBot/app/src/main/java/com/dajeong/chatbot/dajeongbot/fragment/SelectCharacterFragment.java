package com.dajeong.chatbot.dajeongbot.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dajeong.chatbot.dajeongbot.activity.SignupActivity;
import com.dajeong.chatbot.dajeongbot.alias.AccountType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.activity.LoginActivity;
import com.dajeong.chatbot.dajeongbot.activity.MainActivity;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSignUp;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;

import java.io.IOException;
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
    private int currentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_select_bot, container, false);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.view_select_bot, container, false);
        selectImage = new int[]{R.id.img_chatbot_1, R.id.img_chatbot_2, R.id.img_chatbot_3, R.id.img_chatbot_4};
        selectDot = new int[]{R.id.img_select_dot_1, R.id.img_select_dot_2, R.id.img_select_dot_3, R.id.img_select_dot_4};

        introduceHead = new int[]{R.drawable.chatbot_head_1, R.drawable.chatbot_head_2, R.drawable.chatbot_head_3, R.drawable.chatbot_head_4};
        introduceTitle = new int[]{R.drawable.chatbot_text_1, R.drawable.chatbot_text_2, R.drawable.chatbot_text_3, R.drawable.chatbot_text_4};
        introduceString = new int[]{R.string.dajeong_1_info, R.string.dajeong_2_info, R.string.dajeong_3_info, R.string.dajeong_4_info};
        introduceImage = new int[]{R.raw.chatbot_move_image_1, R.raw.chatbot_move_image_2, R.raw.chatbot_move_image_3, R.raw.chatbot_move_image_4};

        mIvBotHead = rootView.findViewById(R.id.img_head);
        mIvBotTitle = rootView.findViewById(R.id.img_title);
        mTvBotString = rootView.findViewById(R.id.text_string);
        mIvBotIntroduce = rootView.findViewById(R.id.img_introduce);
        mLiBtnPrevious = rootView.findViewById(R.id.layout_btn_previous);
        mLiBtnNext = rootView.findViewById(R.id.layout_btn_next);


        btnSelectPrevious = rootView.findViewById(R.id.btn_select_previous);
        btnSelectNext = rootView.findViewById(R.id.btn_select_next);

        Glide.with(this).load(R.raw.chatbot_move_image_1).into(mIvBotIntroduce);
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
            botIntroduceImages.add((ImageView) rootView.findViewById(R.raw.chatbot_move_image_1 + i));
        }

        for (int i = 0; i < 4; i++) {
            botHeads.set(i, (Button) rootView.findViewById(selectImage[i]));
        }
        for (int i = 0; i < botHeads.size(); i++) {
            botHeads.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO TEST 2
                    Button clickButton = (Button) v;
                    int index = botHeads.indexOf(clickButton);

                    if (index == 0) {
                        btnSelectPrevious.setVisibility(View.INVISIBLE);
                        mLiBtnPrevious.setVisibility(View.INVISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        mLiBtnNext.setVisibility(View.VISIBLE);
                    } else if (index == 1 || index == 2) {
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        mLiBtnPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.VISIBLE);
                        mLiBtnNext.setVisibility(View.VISIBLE);
                    } else if (index == 3) {
                        btnSelectPrevious.setVisibility(View.VISIBLE);
                        mLiBtnPrevious.setVisibility(View.VISIBLE);
                        btnSelectNext.setVisibility(View.INVISIBLE);
                        mLiBtnNext.setVisibility(View.INVISIBLE);
                    }
                    currentPage(index);
                    changeVisibleImage(index);
                    changeIntroduceImage(index);
                    Log.e(TAG, "here" + String.valueOf(currentPage));

                }
            });
        }

        btnSelectDone = rootView.findViewById(R.id.btn_select);
        btnSelectDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 필요한 데이터 ( user_id, name, birthday, account_type, bot_type )
                //TODO : API 로 로그인시 첫 유저이면 SIGN ACTIVITY 로
                // 첫 유저일 경우 http://172.30.1.35/apis/users/{user_id}/{password}로 쿼리시 [{"status": "Failed"}] 반환
                //TODO : TOKEN 저장
                String userId = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_email");
                String name = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_name");

                String birthday = concatDate(CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_year"),
                        CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_month"),
                        CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_day"));

                int botType = currentPage;
                int accountType = ((SignupActivity) getActivity()).getAccountType();
                String password = CustomSharedPreference.getInstance(getContext(), "data").getStringPreferences("user_pw");
                String token = ((SignupActivity) getActivity()).getToken();


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
                Call<JsonObject> res = NetRetrofit.getInstance(getActivity().getApplicationContext()).getService().addUserInfo(new RequestSignUp(userId, name, birthday, botType, accountType, password, token));
                res.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        Log.e(TAG, String.valueOf(response.body()));
                        Intent intent = null;

                        if (response.body().get("status").getAsString().equals("Success")) {
                            // 회원가입 성공
                            intent = new Intent(getActivity(), MainActivity.class);

                            Log.e(TAG, "id is "+  response.body().getAsJsonObject("user_info").get("id").getAsString());
                            CustomSharedPreference.getInstance(getContext(), "user_info")
                                    .savePreferences("id", response.body().getAsJsonObject("user_info").get("id").getAsString());
                            CustomSharedPreference.getInstance(getContext(), "user_info")
                                    .savePreferences("bot_type", response.body().getAsJsonObject("user_info").get("bot_type").getAsInt());

                        } else if (response.body().get("status").getAsString().equals("ExistUser")) {
                            // 존재하는 회원
                            Toast.makeText(getContext(), "이미 존재하는 회원입니다.", Toast.LENGTH_LONG).show();
                            intent = new Intent(getActivity(), LoginActivity.class);
                        } else {
                            // 회원가입 실패
                            Toast.makeText(getContext(), "회원가입에 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
                            intent = new Intent(getActivity(), LoginActivity.class);
                        }
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        if (t != null)
                            Log.e(TAG, t.getMessage());
                    }
                });

//                    userCall.enqueue(this);

            }
        });
        // End


        btnSelectPrevious.setOnClickListener(Handler);
        btnSelectNext.setOnClickListener(Handler);
        mLiBtnPrevious.setOnClickListener(Handler);
        mLiBtnNext.setOnClickListener(Handler);
        return rootView;
    }

    public  void currentPage(int index){
        currentPage=index;
        Log.e(TAG,"인덱스"+currentPage);
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
        Glide.with(this).load(R.raw.chatbot_move_image_1+index).into(mIvBotIntroduce);

    }

    private String concatDate(String year, String month, String date) {
        if (month.length() < 2) {
            month = "0" + month;
        }
        if (date.length() < 2) {
            date = "0" + date;
        }
        return year + "-" + month + "-" + date;
    }
    View.OnClickListener Handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_select_previous:
                case R.id.layout_btn_previous:
                    count--;
                    break;
                case R.id.btn_select_next:
                case R.id.layout_btn_next:
                    count++;
                    break;
            }
            ChangeIntroduce();

        }
    };
    private void ChangeIntroduce(){
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
}

