package com.dajeong.chatbot.dajeongbot.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.dajeong.chatbot.dajeongbot.R;

/**
 * Created by s2017 on 2018-08-02.
 */

public class InputNameFragment extends Fragment{
        private EditText InputName;
        Button btn_next;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_input_name, container, false);
        LinearLayout ll = (LinearLayout)inflater.inflate(R.layout.view_input_name, container, false);

        btn_next = rootView.findViewById(R.id.btn_next);

        return rootView;
    }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText inputName = (EditText) getView().findViewById(R.id.edit_input_name);
        Log.e("UserName",inputName.getText().toString());
    }

}
