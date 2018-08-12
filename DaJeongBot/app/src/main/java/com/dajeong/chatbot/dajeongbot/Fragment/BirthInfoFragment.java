package com.dajeong.chatbot.dajeongbot.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dajeong.chatbot.dajeongbot.Activity.SignupActivity;
import com.dajeong.chatbot.dajeongbot.R;

public class BirthInfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_birth_info , container, false);

        rootView.findViewById(R.id.btn_previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        Log.e("username",inputName.getText().toString());
                ((SignupActivity)getActivity()).setCurrentItem(((SignupActivity)getActivity()).getCurrentItem()-1,true);
                new IntroduceFragment();
            }
        });

        rootView.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        Log.e("username",inputName.getText().toString());
                ((SignupActivity)getActivity()).setCurrentItem(((SignupActivity)getActivity()).getCurrentItem()+1,true);
                new IntroduceFragment();
            }
        });

        return rootView;
    }

}
