package com.dajeong.chatbot.dajeongbot.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dajeong.chatbot.dajeongbot.Activity.SignupActivity;
import com.dajeong.chatbot.dajeongbot.Control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.R;

/**
 * Created by s2017 on 2018-08-02.
 */

public class InputNameFragment extends Fragment {
    private EditText inputName;
    private Button btn_input_next;
//    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_input_name, container, false);
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.view_input_name, container, false);

        inputName = rootView.findViewById(R.id.edit_input_name);

        btn_input_next = rootView.findViewById(R.id.btn_check);
        //input data 저장하기

        btn_input_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("username", inputName.getText().toString());
                CustomSharedPreference
                        .getInstance(getContext(), "data")
                        .savePreferences("user_name", inputName.getText().toString());
                ((SignupActivity) getActivity()).setCurrentItem(((SignupActivity) getActivity()).getCurrentItem() + 1, true);
                new IntroduceFragment();
            }
        });


        return rootView;
    }
//        @Override
//        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        inputName = getView().findViewById(R.id.edit_input_name);
//        Log.e("UserName",inputName.getText().toString());
//    }
//
//    private class ButtonEventNext implements View.OnClickListener{
//            @Override
//            public void onClick(View v){
//                int username = Log.e("username", inputName.getText().toString());
//                ((SignupActivity)getActivity()).setCurrentItem(1,true);
//                new IntroduceFragment();
//            }
//
//    }
}
