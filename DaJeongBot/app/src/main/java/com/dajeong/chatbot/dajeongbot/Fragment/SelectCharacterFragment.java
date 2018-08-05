package com.dajeong.chatbot.dajeongbot.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dajeong.chatbot.dajeongbot.R;

/**
 * Created by s2017 on 2018-08-03.
 */

public class SelectCharacterFragment extends Fragment implements View.OnClickListener {
    private Button btn_select_done;
    private Button btn_select_next, btn_select_previous;

    private int count=0;
    private int selectImage[];
    private int selectDot[];

    private int introduceHead[];
    private int introduceTitle[];
    private int introduceString[];
    private int introduceImage[];

    private ImageView chatbotHead, chatbotTitle, chatbotIntroduce;
    private TextView chatbotString;
    private Button chatbotImg_1,chatbotImg_2,chatbotImg_3,chatbotImg_4;
    private ImageView chatbotDot_1, chatbotDot_2, chatbotDot_3, chatbotDot_4;
//    private ImageView chatbotHead_1,chatbotHead_2,chatbotHead_3,chatbotHead_4;
//    private ImageView chatbotTitle_1, chatbotTitle_2, chatbotTitle_3, chatbotTitle_4;
//    private TextView chatbotString_1,chatbotString_2,chatbotString_3,chatbotString_4;
//    private ImageView chatbotIntroduce_1,chatbotIntroduce_2,chatbotIntroduce_3,chatbotIntroduce_4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_select_bot, container, false);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.view_select_bot, container, false);

        selectImage = new int[]{R.id.img_chatbot_1, R.id.img_chatbot_2, R.id.img_chatbot_3, R.id.img_chatbot_4};
        selectDot = new int[]{R.id.img_select_dot_1, R.id.img_select_dot_2, R.id.img_select_dot_3, R.id.img_select_dot_4};

        introduceHead = new int[]{R.drawable.chatbot_head_1, R.drawable.chatbot_head_2, R.drawable.chatbot_head_3, R.drawable.chatbot_head_4};
        introduceTitle = new int[]{R.drawable.chatbot_text_1,R.drawable.chatbot_text_2,R.drawable.chatbot_text_3,R.drawable.chatbot_text_4};
        introduceString = new int[]{R.string.dajeong_1_info, R.string.dajeong_2_info, R.string.dajeong_3_info, R.string.dajeong_4_info};
        introduceImage = new int[]{R.drawable.chatbot_introduce_1, R.drawable.chatbot_introduce_2, R.drawable.chatbot_introduce_3, R.drawable.chatbot_introduce_4};

        chatbotHead = rootView.findViewById(R.id.img_head);
        chatbotTitle = rootView.findViewById(R.id.img_title);
        chatbotString = rootView.findViewById(R.id.text_string);
        chatbotIntroduce = rootView.findViewById(R.id.img_introduce);

        //챗봇 버튼 이미지
        chatbotImg_1 = rootView.findViewById(selectImage[0]);
        chatbotImg_2 = rootView.findViewById(selectImage[1]);
        chatbotImg_3 = rootView.findViewById(selectImage[2]);
        chatbotImg_4 = rootView.findViewById(selectImage[3]);

        // 챗봇 선택 도트
        chatbotDot_1 = rootView.findViewById(selectDot[0]);
        chatbotDot_2 = rootView.findViewById(selectDot[1]);
        chatbotDot_3 = rootView.findViewById(selectDot[2]);
        chatbotDot_4 = rootView.findViewById(selectDot[3]);

//        //HEAD 이미지
//        chatbotHead_1 = rootView.findViewById(introduceHead[0]);
//        chatbotHead_2 = rootView.findViewById(introduceHead[1]);
//        chatbotHead_3 = rootView.findViewById(introduceHead[2]);
//        chatbotHead_4 = rootView.findViewById(introduceHead[3]);
//
//        //TITLE 이미지
//        chatbotTitle_1 = rootView.findViewById(introduceTitle[0]);
//        chatbotTitle_2 = rootView.findViewById(introduceTitle[1]);
//        chatbotTitle_3 = rootView.findViewById(introduceTitle[2]);
//        chatbotTitle_4 = rootView.findViewById(introduceTitle[3]);
//
//        // 챗봇 소개 STRING
//        chatbotString_1 = rootView.findViewById(introduceString[0]);
//        chatbotString_2 = rootView.findViewById(introduceString[1]);
//        chatbotString_3 = rootView.findViewById(introduceString[2]);
//        chatbotString_4 = rootView.findViewById(introduceString[3]);
//
//        // 챗봇 이미지
//        chatbotIntroduce_1 = rootView.findViewById(introduceImage[0]);
//        chatbotIntroduce_2 = rootView.findViewById(introduceImage[1]);
//        chatbotIntroduce_3 = rootView.findViewById(introduceImage[2]);
//        chatbotIntroduce_4 = rootView.findViewById(introduceImage[3]);

        btn_select_done = rootView.findViewById(R.id.btn_select);

        btn_select_previous = rootView.findViewById(R.id.btn_select_previous);
        btn_select_next = rootView.findViewById(R.id.btn_select_next);

        chatbotImg_1.setOnClickListener(this);
        chatbotImg_2.setOnClickListener(this);
        chatbotImg_3.setOnClickListener(this);
        chatbotImg_4.setOnClickListener(this);
        btn_select_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
               switch (count){
                   case 0:
                       btn_select_previous.setVisibility(View.INVISIBLE);
                       btn_select_next.setVisibility(View.VISIBLE);
                       chatbotDot_1.setVisibility(View.VISIBLE);
                       chatbotDot_2.setVisibility(View.INVISIBLE);
                       chatbotDot_3.setVisibility(View.INVISIBLE);
                       chatbotDot_4.setVisibility(View.INVISIBLE);
                       chatbotHead.setImageResource(introduceHead[count]);
                       chatbotTitle.setImageResource(introduceTitle[count]);
                       chatbotString.setText(introduceString[count]);
                       chatbotIntroduce.setImageResource(introduceImage[count]);
                       break;
                   case 1:
                       btn_select_previous.setVisibility(View.VISIBLE);
                       btn_select_next.setVisibility(View.VISIBLE);
                       chatbotDot_1.setVisibility(View.INVISIBLE);
                       chatbotDot_2.setVisibility(View.VISIBLE);
                       chatbotDot_3.setVisibility(View.INVISIBLE);
                       chatbotDot_4.setVisibility(View.INVISIBLE);
                       chatbotHead.setImageResource(introduceHead[count]);
                       chatbotTitle.setImageResource(introduceTitle[count]);
                       chatbotString.setText(introduceString[count]);
                       chatbotIntroduce.setImageResource(introduceImage[count]);
                       break;
                   case 2:
                       btn_select_previous.setVisibility(View.VISIBLE);
                       btn_select_next.setVisibility(View.VISIBLE);
                       chatbotDot_1.setVisibility(View.INVISIBLE);
                       chatbotDot_2.setVisibility(View.INVISIBLE);
                       chatbotDot_3.setVisibility(View.VISIBLE);
                       chatbotDot_4.setVisibility(View.INVISIBLE);
                       chatbotHead.setImageResource(introduceHead[count]);
                       chatbotTitle.setImageResource(introduceTitle[count]);
                       chatbotString.setText(introduceString[count]);
                       chatbotIntroduce.setImageResource(introduceImage[count]);
                       break;
                   case 3:
                       btn_select_previous.setVisibility(View.VISIBLE);
                       btn_select_next.setVisibility(View.INVISIBLE);
                       chatbotDot_1.setVisibility(View.INVISIBLE);
                       chatbotDot_2.setVisibility(View.INVISIBLE);
                       chatbotDot_3.setVisibility(View.INVISIBLE);
                       chatbotDot_4.setVisibility(View.VISIBLE);
                       chatbotHead.setImageResource(introduceHead[count]);
                       chatbotTitle.setImageResource(introduceTitle[count]);
                       chatbotString.setText(introduceString[count]);
                       chatbotIntroduce.setImageResource(introduceImage[count]);
                       break;
               }

            }
        });
        btn_select_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                switch (count){
                    case 0:
                        btn_select_previous.setVisibility(View.INVISIBLE);
                        chatbotDot_1.setVisibility(View.VISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        chatbotHead.setImageResource(introduceHead[count]);
                        chatbotTitle.setImageResource(introduceTitle[count]);
                        chatbotString.setText(introduceString[count]);
                        chatbotIntroduce.setImageResource(introduceImage[count]);
                        break;
                    case 1:
                        btn_select_previous.setVisibility(View.VISIBLE);
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.VISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        chatbotHead.setImageResource(introduceHead[count]);
                        chatbotTitle.setImageResource(introduceTitle[count]);
                        chatbotString.setText(introduceString[count]);
                        chatbotIntroduce.setImageResource(introduceImage[count]);
                        break;
                    case 2:
                        btn_select_previous.setVisibility(View.VISIBLE);
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.VISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        chatbotHead.setImageResource(introduceHead[count]);
                        chatbotTitle.setImageResource(introduceTitle[count]);
                        chatbotString.setText(introduceString[count]);
                        chatbotIntroduce.setImageResource(introduceImage[count]);
                        break;
                    case 3:
                        btn_select_previous.setVisibility(View.VISIBLE);
                        btn_select_next.setVisibility(View.INVISIBLE);
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.VISIBLE);
                        chatbotHead.setImageResource(introduceHead[count]);
                        chatbotTitle.setImageResource(introduceTitle[count]);
                        chatbotString.setText(introduceString[count]);
                        chatbotIntroduce.setImageResource(introduceImage[count]);
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
                        chatbotDot_1.setVisibility(View.VISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        btn_select_previous.setVisibility(View.INVISIBLE);
                        btn_select_next.setVisibility(View.VISIBLE);
                        chatbotHead.setImageResource(R.drawable.chatbot_head_1);
                        chatbotTitle.setImageResource(R.drawable.chatbot_text_1);
                        chatbotString.setText(R.string.dajeong_1_info);
                        chatbotIntroduce.setImageResource(R.drawable.chatbot_introduce_1);
                        break;
                    case R.id.img_chatbot_2:
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.VISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        btn_select_previous.setVisibility(View.VISIBLE);
                        btn_select_next.setVisibility(View.VISIBLE);
                        chatbotHead.setImageResource(R.drawable.chatbot_head_2);
                        chatbotTitle.setImageResource(R.drawable.chatbot_text_2);
                        chatbotString.setText(R.string.dajeong_2_info);
                        chatbotIntroduce.setImageResource(R.drawable.chatbot_introduce_2);
                        break;
                    case R.id.img_chatbot_3:
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.VISIBLE);
                        chatbotDot_4.setVisibility(View.INVISIBLE);
                        btn_select_previous.setVisibility(View.VISIBLE);
                        btn_select_next.setVisibility(View.VISIBLE);
                        chatbotHead.setImageResource(R.drawable.chatbot_head_3);
                        chatbotTitle.setImageResource(R.drawable.chatbot_text_3);
                        chatbotString.setText(R.string.dajeong_3_info);
                        chatbotIntroduce.setImageResource(R.drawable.chatbot_introduce_3);
                        break;
                    case R.id.img_chatbot_4:
                        chatbotDot_1.setVisibility(View.INVISIBLE);
                        chatbotDot_2.setVisibility(View.INVISIBLE);
                        chatbotDot_3.setVisibility(View.INVISIBLE);
                        chatbotDot_4.setVisibility(View.VISIBLE);
                        btn_select_previous.setVisibility(View.VISIBLE);
                        btn_select_next.setVisibility(View.INVISIBLE);
                        chatbotHead.setImageResource(R.drawable.chatbot_head_4);
                        chatbotTitle.setImageResource(R.drawable.chatbot_text_4);
                        chatbotString.setText(R.string.dajeong_4_info);
                        chatbotIntroduce.setImageResource(R.drawable.chatbot_introduce_4);
                        break;
                }
        }
}
