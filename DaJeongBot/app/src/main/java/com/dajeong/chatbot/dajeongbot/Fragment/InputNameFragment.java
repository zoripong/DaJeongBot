package com.dajeong.chatbot.dajeongbot.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dajeong.chatbot.dajeongbot.Activity.InputInfoActivity;
import com.dajeong.chatbot.dajeongbot.R;

/**
 * Created by s2017 on 2018-08-02.
 */

public class InputNameFragment extends Fragment{
        private EditText inputName;
        private Button btn_input_next;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_input_name, container, false);
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.view_input_name, container, false);

        inputName = rootView.findViewById(R.id.edit_input_name);

        btn_input_next = rootView.findViewById(R.id.btn_input_next);
        btn_input_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("username",inputName.getText().toString());
                ((InputInfoActivity)getActivity()).setCurrentItem(1,true);
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
//                ((InputInfoActivity)getActivity()).setCurrentItem(1,true);
//                new IntroduceFragment();
//            }
//
//    }
}
