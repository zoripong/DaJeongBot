package com.dajeong.chatbot.dajeongbot.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.Activity.SignupActivity;
import com.dajeong.chatbot.dajeongbot.Control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by s2017 on 2018-08-15.
 */

public class SignUpFragment extends Fragment {

    private EditText inputEmail, inputPw;
    private String Year, Month, Day;
    private boolean isYearSelect = false, isMonthSelect = false;
    private Spinner yearSpinner, monthSpinner, daySpinner;
    private String[] year, month, day;
    private List<String> yearList, monthList, dayList;
    private ArrayAdapter<String> yearAdapter, monthAdapter, dayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.view_sign_up, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btnCheck = getView().findViewById(R.id.btn_check);

        inputEmail = getView().findViewById(R.id.etUserEmail);
        inputPw = getView().findViewById(R.id.etUserPw);

        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        // YEAR
        yearSpinner = (Spinner) getView().findViewById(R.id.spinner_year);
        year = new String[]{};
        yearList = new ArrayList<>(Arrays.asList(year));
        yearAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        yearSpinner.setAdapter(yearAdapter);
        for (int i = 1900; i <= 2018; i++) {
            yearList.add(Integer.toString(i));
            yearAdapter.notifyDataSetChanged();
        }
        yearSpinner.setSelection(100); // 2000년

        //MONTH
        monthSpinner = (Spinner) getView().findViewById(R.id.spinner_month);
        month = new String[]{};
        monthList = new ArrayList<>(Arrays.asList(month));
        monthAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        monthSpinner.setAdapter(monthAdapter);
        setBirthdayList(12, monthList, monthAdapter);

        //DAY
        daySpinner = (Spinner) getView().findViewById(R.id.spinner_day);
        day = new String[]{};
        dayList = new ArrayList<>(Arrays.asList(day));
        dayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        daySpinner.setAdapter(dayAdapter);


        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelect = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Year = yearSpinner.getSelectedItem().toString();
                isYearSelect = !isFirstSelect;
                isFirstSelect = false;
                checkIsEnabled(daySpinner);
                Log.e("UserYear", Year);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean isFirstSelect = true;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Month = monthSpinner.getSelectedItem().toString();
                Log.e("UserMonth", Month);
                isMonthSelect = !isFirstSelect;
                isFirstSelect = false;
                checkIsEnabled(daySpinner);
                dayList.clear();
                if (Month.equals("4") || Month.equals("6") || Month.equals("9") || Month.equals("11")) {
                    setBirthdayList(30, dayList, dayAdapter);
                } else if (Month.equals("2") && isLeapYear(Year) == false) {
                    setBirthdayList(28, dayList, dayAdapter);
                } else if (Month.equals("2") && isLeapYear(Year) == true) {
                    setBirthdayList(29, dayList, dayAdapter);
                } else {
                    setBirthdayList(31, dayList, dayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                daySpinner.getSelectedView();
            }
        });
        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Day = daySpinner.getSelectedItem().toString();
                Log.e("UserDay", Day + "  " + daySpinner.isEnabled());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                effectivenessCheck();

                if (effectivenessCheck() == true) {
                    Toast.makeText(getActivity(), "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();

                    Log.e("UserEmail", inputEmail.getText().toString());
                    Log.e("UserPw", inputPw.getText().toString());
                    Log.e("UserYear", Year);
                    Log.e("UserMonth", Month);
                    Log.e("UserDay", Day);

                    // Data 저장(Email, Pw, Year, Month, Day)
                    CustomSharedPreference
                            .getInstance(getContext(), "data")
                            .savePreferences("user_email", inputEmail.getText().toString());
                    CustomSharedPreference
                            .getInstance(getContext(), "data")
                            .savePreferences("user_pw", inputPw.getText().toString());
                    CustomSharedPreference
                            .getInstance(getContext(), "data")
                            .savePreferences("user_year", Year);
                    CustomSharedPreference
                            .getInstance(getContext(), "data")
                            .savePreferences("user_month", Month);
                    CustomSharedPreference
                            .getInstance(getContext(), "data")
                            .savePreferences("user_day", Day);

                    ((SignupActivity) getActivity()).setCurrentItem(((SignupActivity) getActivity()).getCurrentItem() + 1, true);
                    new InputNameFragment();
                }
            }
        });
    }

    private boolean effectivenessCheck() { // 유효성 검사
        if (inputEmail.getText().toString().compareToIgnoreCase("") == 0 ||
                !Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString()).matches()) {
            Toast.makeText(getActivity(), "Email을 확인해주세요", Toast.LENGTH_SHORT).show();
            inputEmail.setText("");
            inputEmail.requestFocus();
            return false;
        } else if (inputPw.getText().toString().compareToIgnoreCase("") == 0 ||
                !Pattern.matches("^[A-Za-z0-9]*.{8,16}$", inputPw.getText().toString())) {
            Toast.makeText(getActivity(), "Password를 확인해주세요", Toast.LENGTH_SHORT).show();
            inputPw.setText("");
            inputPw.requestFocus();
            return false;
        } else if (!(isMonthSelect && isYearSelect))
            return false;
        else
            return true;
    }

    private void setBirthdayList(int endDay, List<String> birthdayList, ArrayAdapter<String> birthdayAdapter) {
        for (int i = 1; i <= endDay; i++) {
            birthdayList.add(Integer.toString(i));
            birthdayAdapter.notifyDataSetChanged();
        }
    }

    private void checkIsEnabled(Spinner daySpinner) {
        daySpinner.setEnabled(isYearSelect && isMonthSelect);
    }

    private boolean isLeapYear(String year) { // 윤달 체크
        int iYear = Integer.parseInt(year);
        return ((iYear % 4 == 0) && (iYear % 100 != 0)) || (iYear % 400 == 0);
    }
}
