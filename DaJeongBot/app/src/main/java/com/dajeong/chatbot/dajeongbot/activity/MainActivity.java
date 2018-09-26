package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.AWSMobileController;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.control.MessageReader;
import com.dajeong.chatbot.dajeongbot.control.MessageReceiver;
import com.dajeong.chatbot.dajeongbot.customize.AutoDialog;
import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.GalleryImage;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSendMessage;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.io.File;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 어떻게하는거지? 튜토리얼 띄우기
// 메인 채팅 화면 activity
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "MainActivity";


    protected static Context mContext;
    //component
    protected EditText mEtMessage;
    private FloatingActionButton mFabDown;

    //자동완성
    private ConstraintLayout mainBottom;
    //언제
    private ImageView whenCloseBtn;
    private ImageView whenBtnSend;
    private EditText messageY;
    private EditText messageM;
    private EditText messageD;

    //어디서 무엇을
    private EditText whereWhatMessage;
    private ImageButton btnWhereWhatSend;
    private int checkWhere=-1; //무엇을을 입력했는지 확인
    private TextView whereWhatText;

    //추억회상
    private boolean checkRecall=false;
    private TextView botName;
    // recycler view
    private LinkedList<Chat> mChats;
    private RecyclerView mRvChatList;
    private ChatAdapter mChatAdapter;

    private Character mBotChar;

    private int mAccountId;
    private boolean mIsLoad;
    private boolean mMoreChat;
    private int mChatType;
    private int mSelectIndex;

    private JsonObject mJsonResponse;
    private HashMap<String, Integer> mStringNodeTypeMap;

    private AWSMobileController mAWSctlr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onNewIntent(getIntent());

        init();
        bottominit();
        getMessage();
        showProgressBar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvChatList.setHasFixedSize(true);
        mRvChatList.setLayoutManager(layoutManager);

        mRvChatList.setAdapter(mChatAdapter);
//        Context context = mRvChatList.getContext();
//        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_from_bottom);

//        mRvChatList.setLayoutAnimation(controller);
//        mRvChatList.getAdapter().notifyDataSetChanged();
//        mRvChatList.scheduleLayoutAnimation();



        mRvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRvChatList.canScrollVertically(-1)) {
//                    Log.e(TAG, "OnScrolled : TOP");
                    if (mIsLoad && mMoreChat) {
                        showProgressBar();
                        getMoreMessage();
                    }
                }
                if(mRvChatList.canScrollVertically(0)){
                    // fab가 보여야하는데 안보일때
                    if(!mFabDown.isShown()){
                        mFabDown.show();
                    }
                    Log.e(TAG, "Bottom nono");

                }else{
                    if(mFabDown.isShown()){
                        mFabDown.hide();
                    }
                }


                //위로 스크롤시 이름이 없어지고 아래로 스크롤하면 다시 이름 생김
                if (dy > 0 && botName.getVisibility() != View.VISIBLE) {
//                    Animation animation = new AlphaAnimation(0, 1); //이거는 사르륵 효과
//                    animation.setDuration(500);
                    Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                            0.0f, Animation.RELATIVE_TO_PARENT, -0.5f, Animation.RELATIVE_TO_PARENT, 0.0f);
                    animation.setDuration(500);
                    botName.setVisibility(View.VISIBLE);
                    botName.setAnimation(animation);
                } else if (dy < 0 && botName.getVisibility() == View.VISIBLE) {
//                    Animation animation = new AlphaAnimation(1, 0);
//                    animation.setDuration(500);
                    Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                            0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);
                    animation.setDuration(1500);
                    botName.setVisibility(View.GONE);
                    botName.setAnimation(animation);
                }
            }
        });


        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSendMessage(mEtMessage.getText().toString());
            }


        });


        findViewById(R.id.ivAddChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AutoDialog(MainActivity.this);
            }
        });

        findViewById(R.id.ivAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivityForResult(intent,4000);

            }
        });

        mFabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRvChatList.smoothScrollToPosition(mChatAdapter.getItemCount() - 1);
                mFabDown.hide();
            }
        });

        showTutorial();
        this.setFinishOnTouchOutside(false);
    }

    public void clickSendMessage(String message){
        String getMessage=message;
//        Log.e(TAG,"입력된 메시지 : "+getMessage);
        if (getMessage != null && !getMessage.replace(" ", "").equals("")) {
            // TODO : 추가 할 때 애니메이션
            int accountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
            String content = String.valueOf(getMessage);
            int chatType = mChatType;
            long time = System.currentTimeMillis();
            int isBot = 0;

            if(mChatType == ChatType.QUESTION_SCHEDULE_REPLY_CHAT){
                content = String.valueOf(mSelectIndex)+":"+content;
            }
            sendMessage(accountId, content, NodeType.SPEAK_NODE, chatType, String.valueOf(time), isBot);
//                    runLayoutAnimation(mRvChatList);
            mChats.add(new Chat(NodeType.SPEAK_NODE, null, getMessage , String.valueOf(System.currentTimeMillis())));
            mChatAdapter.notifyDataSetChanged();
            mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
            mEtMessage.setText("");
        }
    }
    private void showTutorial() { //dialog로 띄움
        //spm.removePreferences("SHOW_TUTORIAL");
        if(CustomSharedPreference.getInstance(mContext, "MyPrefs").getBoolPreferences("SHOW_TUTORIAL")){
            //Log.e(TAG,"이미 튜토리얼을 보여줌 ->"+spm.getBoolPreferences("SHOW_TUTORIAL"));
            return;
        }
        else{
//            Log.e(TAG,"튜토리얼 안 보여줌 ->"+spm.getBoolPreferences("SHOW_TUTORIAL"));
        }

        CustomSharedPreference.getInstance(mContext, "MyPrefs").savePreferences("SHOW_TUTORIAL", true);
        //spm.removePreferences("SHOW_TUTORIAL");
        startActivity(new Intent(this, TutorialActivity.class));

    }
//    private void runLayoutAnimation(RecyclerView recyclerView){
//        final Context context = mEtMessage.getContext();
//        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_from_bottom);
//
//        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewActionBar = inflater.inflate(R.layout.custom_main_actionbar, null);

        actionBar.setCustomView(viewActionBar);

        // 액션바에 백그라운드 이미지를 아래처럼 입힐 수 있습니다.
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_main_top_background));


        // 이벤트 달기
        viewActionBar.findViewById(R.id.ivCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivityForResult(intent,3000);
            }
        });

        viewActionBar.findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivityForResult(intent,5000);
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

    // CalendarActivity 에서 처리된 결과를 받는 메소드
    // 처리된 결과 코드 (resultCode) 가 RESULT_OK 이면 requestCode 를 판별해 결과 처리를 진행한다.
    // CalculateActivity 에서 처리 결과가 담겨온 데이터를 TextView 에 보여준다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
            // MainActivity 에서 요청할 때 보낸 요청 코드 (3000)
                case 3000: // 캘린더
                    Log.e(TAG,"이벤트 ID : "+data.getStringExtra("result"));
                    sendMessage(mAccountId, data.getStringExtra("result"), NodeType.SPEAK_NODE, ChatType.UPDATE_SCHEDULE_BY_CALENDAR, String.valueOf(System.currentTimeMillis()), 0);
                    break;
                case 4000: // 이미지
                    String INTENT_PHOTO_EXTRA = "INTENT_PHOTO_EXTRA";
                    ArrayList<GalleryImage> selectedList = (ArrayList<GalleryImage>) data.getSerializableExtra(INTENT_PHOTO_EXTRA);
                    Log.e(TAG, "전송된 이미지 사이즈 : "+selectedList.size());
                    for(GalleryImage image : selectedList){
                        AWSMobileController.getInstance(MainActivity.this).uploadWithTransferUtility(mAccountId, new File(image.getImgPath()));
                    }
                    mChatAdapter.notifyDataSetChanged();

                    break;
                case 5000: // 세팅 - 초기화
                    String INTENT_RESET = "INTENT_RESET";
                    if(data.getIntExtra(INTENT_RESET, -1) == 0){
                        mChats.clear();
                        mChatAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }


    private void init() {
        mContext=this;
        mainBottom=(ConstraintLayout) findViewById(R.id.main_bottom);
        mEtMessage = findViewById(R.id.etMessage);
        mFabDown = findViewById(R.id.fabBottom);


        mChats = new LinkedList<>();
        mRvChatList = findViewById(R.id.rvChatList);
        mChatAdapter = new ChatAdapter(getSupportFragmentManager(), mChats, MainActivity.this);

        mBotChar = setBot();
        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
        mIsLoad = false;
        mMoreChat = true;
        mChatType = ChatType.BASIC_CHAT;
        mSelectIndex = -1;

        mJsonResponse = new JsonObject();
        mStringNodeTypeMap = new HashMap<>();
        mStringNodeTypeMap.put("speak", NodeType.SPEAK_NODE);
        mStringNodeTypeMap.put("slot", NodeType.SLOT_NODE);
        mStringNodeTypeMap.put("carousel", NodeType.CAROUSEL_NODE);

        mAWSctlr = AWSMobileController.getInstance(this);

    }

    private void bottominit(){
        //언제

        whenCloseBtn = (ImageView) findViewById(R.id.ivWhenCloseBtn);
        messageY = (EditText) findViewById(R.id.etYear);
        messageM = (EditText) findViewById(R.id.etMonth);
        messageD = (EditText) findViewById(R.id.etDay);
        whenBtnSend = (ImageView) findViewById(R.id.btnWhenSend);

        //어디서 //무엇을
        whereWhatMessage = (EditText) findViewById(R.id.etWhereWhatMessage);
        btnWhereWhatSend = (ImageButton) findViewById(R.id.btnWhereWhatSend);
        whereWhatText= (TextView) findViewById(R.id.etWhereWhatText);
        whenCloseBtn.setOnClickListener(this);
        whenBtnSend.setOnClickListener(this);
        btnWhereWhatSend.setOnClickListener(this);



    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String notificationMessage = extras.getString("data", "UNDEFINED");
        try {
            JSONObject jsonObject = new JSONObject(notificationMessage);
            //Log.e(TAG, jsonObject.getString("status"));
            if(jsonObject.has("status")){
                // FCM Message로 Open
            }
            Toast.makeText(getApplicationContext(), jsonObject.getString("status"), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Log.e(TAG, notificationMessage);
    }

    @NonNull
    private Character setBot(){
        String charNames[] = {"다정군", "다정냥", "다정곰", "다정뭉"};
        int charType = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");
        int charImage = R.drawable.ic_char1 + (charType%4);
        botName = (TextView) findViewById(R.id.tvbotName);
        botName.setText(charNames[charType]);
        return new Character(charNames[charType], charImage);
    }

    private void getMessage(){
        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getMessages(mAccountId);
        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                controlJsonObj(response);
                // FIXME
//                mRvChatList.scrollToPosition(mChatAdapter.getItemCount() );
                mIsLoad = true;

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG, t.toString());
                }
            }
        });
    }

    private void getMoreMessage(){
        int lastIndex = CustomSharedPreference.getInstance(getApplicationContext(), "chat").getIntPreferences("last_index");

        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getMessages(mAccountId, lastIndex);
        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                controlJsonObj(response);
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG, t.toString());
                }
            }
        });
    }

    private void controlJsonObj(Response<ArrayList<JsonObject>> response){
//        Log.e(TAG, "HERE"+String.valueOf(mAccountId));

        ArrayList<JsonObject> body = response.body();
        for(int i = 0 ; i<body.size(); i++) {
            JsonObject json = body.get(i).getAsJsonObject();
            if (json.get("carousel_list").isJsonNull() && json.get("slot_list").isJsonNull()) {
                // carousel_list 와 slot list 가 비어있을 경우
                MessageReader.getInstance().readBasicMessage(json, mChats, mBotChar);
            } else {
                if (!json.get("carousel_list").isJsonNull() && json.get("slot_list").isJsonNull()) {
                    MessageReader.getInstance().readCarouselMessage(json, mChats, mBotChar);
                } else if (!json.get("slot_list").isJsonNull() && json.get("carousel_list").isJsonNull()) {
                    MessageReader.getInstance().readSlotMessage(json, mChats, mBotChar);
                } else {
                    Log.e(TAG, "error");
                }
            }
        }
        if(body.size() == 0 ){
            // 더이상의 대화 내역이 없음
            mMoreChat = false;
        }else{
            // 마지막 인덱스 저장
            CustomSharedPreference.getInstance(getApplicationContext(), "chat").savePreferences("last_index", (body.get(body.size()-1)).get("id").getAsInt());
            mChatAdapter.notifyDataSetChanged();
        }
        hideProgressBar();
    }

    public void sendMessage(int accountId, String content, int nodeType, int chatType, String time, int isBot) {
        int botType = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");
        RequestSendMessage param = new RequestSendMessage(accountId, content,nodeType, chatType, botType, time, isBot, mJsonResponse);
        Call<JsonObject> res = NetRetrofit
                .getInstance(getApplicationContext())
                .getService()
                .sendMessage(param);
        Log.e(TAG, "<REQUEST>"+param.toString());
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e(TAG, String.valueOf(response));
                //TODO : 대화 하다가 앱 종료시 이어서 가능하도록
                if(response.body() != null){
                    Log.e(TAG, "RESPONSE" + String.valueOf(response.body().toString()));

                    if(response.body().has("status")){

                        if(response.body().get("status").getAsString().equals("Failed")){

                            Toast.makeText(getApplicationContext(), "서버에 문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();

                        }else if(response.body().get("status").getAsString().equals("Success")){

                            // custom chatbot api
                            if(response.body().has("result")){
                                JsonObject result = response.body().getAsJsonObject("result");
                                mJsonResponse = result;
                                // receive

                                mChatType = result.get("chat_type").getAsInt();
                                switch (result.get("chat_type").getAsInt()){
                                    case ChatType.BASIC_CHAT:
                                        MessageReceiver.getInstance().receiveBasicMessage(result, mChats, mBotChar);
                                        break;
                                    case ChatType.MEMORY_CHAT:
                                        // TODO TEST
                                        MessageReceiver.getInstance().receiveCarouselMessage(result, mChats, mBotChar);
                                        break;
                                    case ChatType.QUESTION_SCHEDULE_SELECT_CHAT: // 4
                                        // 선택한 일정에 대해 후기를 남겨야 함
                                        // reply_message_for_select_review 를 통해 데이터가 넘어옴
                                        mSelectIndex = result.get("events").getAsJsonArray().get(0).getAsJsonObject().get("id").getAsInt();
                                        mJsonResponse = new JsonParser().parse("{\"select_idx\":"+mSelectIndex+"}").getAsJsonObject();
                                        mChatType = ChatType.QUESTION_SCHEDULE_REPLY_CHAT;
                                        MessageReceiver.getInstance().receiveBasicMessage(result, mChats, mBotChar);
                                        break;
                                    case ChatType.QUESTION_SCHEDULE_REPLY_CHAT: // 5
                                        // reply_message_for_reply_review 를 통해 데이터가 넘어옴
                                        // 일정을 선택해야 함
                                        mChatType = ChatType.QUESTION_SCHEDULE_SELECT_CHAT;

                                        MessageReceiver.getInstance().receiveSlotMessage(result, mChats, mBotChar);
                                        break;

                                }
                            }
                        }

                    }else if(response.body().has("responseSet")){
                        // 단비 api와 대화
                        mChatType = ChatType.BASIC_CHAT;
                        mJsonResponse = response.body().getAsJsonObject("responseSet").getAsJsonObject("result");
                        MessageReceiver.getInstance().receiveDanbeeMessage(mJsonResponse, mChats, mBotChar);
                    }
                    mChatAdapter.notifyDataSetChanged();
                    mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }else{
                    Toast.makeText(getApplicationContext(), "서버에 문제가 발생하였습니다.", Toast.LENGTH_LONG).show();
//                    Log.e(TAG, "a" + response.body().toString());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG, "B" + t.toString());
                }
            }
        });

    }

    private void showProgressBar(){
        findViewById(R.id.pgb).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        findViewById(R.id.pgb).setVisibility(View.INVISIBLE);
    }

    public void setSelectIndex(int index){
        this.mSelectIndex = index;
    }

    public int getSelectIndex(){
        return mSelectIndex;
    }

    public void setSendEditText(boolean status){
        mEtMessage.setEnabled(status);
        if(status){
            mEtMessage.setHint("메세지를 입력해주세요!");
        }else{
            mEtMessage.setHint("챗봇 메세지의 버튼을 이용해주세요!");
        }
    }

    public void setChatType(int chatType){
        this.mChatType = chatType;
    }
    public void setJsonResponse(JsonObject jsonObject){
        this.mJsonResponse = jsonObject;
    }
    public void test(){
        mChats.addFirst(new Chat(NodeType.CAROUSEL_NODE, mBotChar, "골라봐!", String.valueOf(System.currentTimeMillis())));
    }

    public void addAutoSchedule(){
        findViewById(R.id.auto_when).setVisibility(View.VISIBLE);
        findViewById(R.id.year_constraint).setVisibility(View.VISIBLE);
        messageY.setText("");
        messageM.setText("");
        messageD.setText("");
        findViewById(R.id.whereWhat_constraint).setVisibility(View.GONE);
    }
    public void showWhatMessage(){
        findViewById(R.id.year_constraint).setVisibility(View.GONE);
        findViewById(R.id.whereWhat_constraint).setVisibility(View.VISIBLE);
        whereWhatMessage.requestFocus();
    }
    public void addAutoRecall(){
        checkRecall=true;
        addAutoSchedule();
    }
    public void onClick(View view) {
        String inputMessage;
        switch (view.getId()) {
            case R.id.ivWhenCloseBtn:
                inputMessage="취소할래";
                ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                findViewById(R.id.auto_when).setVisibility(View.GONE);
                break;
            case R.id.btnWhenSend:
                if (isNumeric(messageY.getText().toString()) && isNumeric(messageM.getText().toString()) && isNumeric(messageD.getText().toString())) {
                    int inputY = Integer.parseInt(messageY.getText().toString());
                    StringBuilder inputM = new StringBuilder(messageM.getText().toString());
                    if (messageM.getText().toString().charAt(0) == '0') {
                        inputM.deleteCharAt(0);
                    }
                    StringBuilder inputD = new StringBuilder(messageD.getText().toString());
                    if (messageD.getText().toString().charAt(0) == '0') {
                        inputD.deleteCharAt(0);
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar c1 = Calendar.getInstance();
                    String strToday = sdf.format(c1.getTime());

                    Calendar inputDate = Calendar.getInstance();
                    inputDate.set(Calendar.YEAR,inputY);
                    inputDate.set(Calendar.MONTH,Integer.parseInt(inputM.toString())-1);
                    inputDate.set(Calendar.DAY_OF_MONTH,Integer.parseInt(inputD.toString()));
                    String strInputToday = sdf.format(inputDate.getTime());

                     int result=c1.compareTo(inputDate);

                    if(checkRecall==false) {
                        if (inputY >= 2000 && inputY < 3000 && result==-1 &&
                                Integer.parseInt(inputM.toString()) >= 1 && Integer.parseInt(inputM.toString()) <= 12 &&
                                Integer.parseInt(inputD.toString()) >= 1 && Integer.parseInt(inputD.toString()) <= 31 || result==0 ) {
                            inputMessage = messageY.getText().toString() + "년 " + messageM.getText().toString() + "월 " + messageD.getText().toString();
                            ((MainActivity) MainActivity.mContext).clickSendMessage(inputMessage);
                            showWhatMessage();
                        } else {
                            Toast.makeText(mContext, "오늘 또는 앞으로의 날짜를 입력해주세요!", Toast.LENGTH_LONG).show();
                            Log.e("날짜ㅏ",Integer.toString(result)+"들어간 날짜 : "+strInputToday+"오늘날짜:"+strToday);
                        }
                    }
                    else { //추억 회상인 경우
                        if (inputY >= 2000 && inputY < 3000 && result==1 &&
                                Integer.parseInt(inputM.toString()) >= 1 && Integer.parseInt(inputM.toString()) <= 12 &&
                                Integer.parseInt(inputD.toString()) >= 1 && Integer.parseInt(inputD.toString()) <= 31) {
                            inputMessage = messageY.getText().toString() + "년 " + messageM.getText().toString() + "월 " + messageD.getText().toString();
                            ((MainActivity)mContext).clickSendMessage(inputMessage);
                            findViewById(R.id.year_constraint).setVisibility(View.GONE);
                            findViewById(R.id.whereWhat_constraint).setVisibility(View.GONE);
                            findViewById(R.id.auto_when).setVisibility(View.GONE);
                            checkRecall=false;
                        }else {
                            Toast.makeText(mContext, "지나간 날짜를 입력해주세요!", Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    Toast.makeText(mContext, "올바른 날짜를 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnWhereWhatSend :
                if (whereWhatMessage.getText() != null && !whereWhatMessage.getText().toString().replace(" ", "").equals("")) {
                    checkWhere++;
                    if(checkWhere==1) { //어디서를 입력하고 무엇을도 입력한 경우 일정 등록 완료
                        inputMessage=whereWhatMessage.getText().toString();
                        ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                        whereWhatMessage.setText("");
                        findViewById(R.id.year_constraint).setVisibility(View.GONE);
                        findViewById(R.id.whereWhat_constraint).setVisibility(View.GONE);
                        findViewById(R.id.auto_when).setVisibility(View.GONE);
                        checkWhere=-1;
                    }
                    else if(checkWhere==0){
                        inputMessage=whereWhatMessage.getText().toString();
                        ((MainActivity)MainActivity.mContext).clickSendMessage(inputMessage);
                        whereWhatMessage.setText("");
                        whereWhatText.setText("을(를)하는거야!");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "메시지를 입력해주세요!", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    public void addUserImageChat(String path){
        String time = String.valueOf(System.currentTimeMillis());
        mChats.addLast(new Chat(NodeType.IMAGE_NODE, null, path, time));
        mChatAdapter.notifyDataSetChanged();
        sendMessage(mAccountId, path, NodeType.IMAGE_NODE, ChatType.BASIC_CHAT, time, 0 );
        //sendMessage(int accountId, String content, int chatType, String time, int isBot)
    }
    public static boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }
}
