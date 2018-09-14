package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.control.MessageReader;
import com.dajeong.chatbot.dajeongbot.control.MessageReceiver;
import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSendMessage;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO: 어떻게하는거지? 튜토리얼 띄우기
// 메인 채팅 화면 activity
public class MainActivity extends AppCompatActivity  {
    private final String TAG = "MainActivity";
    //component
    private EditText mEtMessage;
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

    private CustomSharedPreference spm; // TODO : FIX...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onNewIntent(getIntent());

        init();
//        test();
        getMessage();
        showProgressBar();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvChatList.setHasFixedSize(true);
        mRvChatList.setLayoutManager(layoutManager);

        mRvChatList.setAdapter(mChatAdapter);

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
            }
        });


        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEtMessage.getText() != null && !mEtMessage.getText().toString().replace(" ", "").equals("")) {
                    // TODO : 추가 할 때 애니메이션
                    int accountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
                    String content = String.valueOf(mEtMessage.getText());
                    int chatType = mChatType;
                    long time = System.currentTimeMillis();
                    int isBot = 0;

                    if(mChatType == ChatType.QUESTION_SCHEDULE_REPLY_CHAT){
                        content = String.valueOf(mSelectIndex)+":"+content;
                    }
                    sendMessage(accountId, content, chatType, String.valueOf(time), isBot);
//                    runLayoutAnimation(mRvChatList);
                    mChats.add(new Chat(NodeType.SPEAK_NODE, null, mEtMessage.getText().toString() , String.valueOf(System.currentTimeMillis())));
                    mChatAdapter.notifyDataSetChanged();
                    mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    mEtMessage.setText("");
                }

            }


        });


        // FIXME
        findViewById(R.id.ivAddChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "아주 잠시만 기다려주세요! 더 원활한 대화를 위해 자동완성 기능을 제공해드릴게요!", Toast.LENGTH_LONG).show();

            }
        });

        findViewById(R.id.ivAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
//                startActivity(intent);
                Toast.makeText(getApplicationContext(), "아주 잠시만 기다려주세요! 더 생생한 기억을 위해 이미지 저장 기능을 제공해드릴게요!", Toast.LENGTH_LONG).show();

            }
        });

        showTutorial();
        this.setFinishOnTouchOutside(false);
    }

    private void showTutorial() { //dialog로 띄움

        spm = new CustomSharedPreference(this);
        //spm.removePreferences("SHOW_TUTORIAL");
        if(spm.retrieveBoolean("SHOW_TUTORIAL")){
            //Log.e(TAG,"이미 튜토리얼을 보여줌 ->"+spm.retrieveBoolean("SHOW_TUTORIAL"));
            return;
        }
        else{
//            Log.e(TAG,"튜토리얼 안 보여줌 ->"+spm.retrieveBoolean("SHOW_TUTORIAL"));
        }

        spm.savePreferences("SHOW_TUTORIAL", true);
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
                startActivity(intent);
                finish();
            }
        });

        viewActionBar.findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)viewActionBar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

    private void init() {
        mEtMessage = findViewById(R.id.etMessage);
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
        int charImage;
        Log.e(TAG,"CharType >>"+charType);
        switch (charType){
            case 0:
                charImage=R.drawable.ic_char1;
                break;
            case 1:
                charImage=R.drawable.ic_char2;
                break;
            case 2:
                charImage=R.drawable.ic_char3;
                break;
            case 3:
                charImage=R.drawable.ic_char4;
                break;
                default:
                    charImage=R.drawable.ic_char1;
                    break;
        }
        TextView botName = (TextView) findViewById(R.id.tvbotName);
        botName.setText(charNames[charType]);
        return new Character(charNames[charType], charImage);
    }

    private void getMessage(){
        Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getMessages(mAccountId);
        res.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                controlJsonObj(response);
                mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
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

    public void sendMessage(int accountId, String content, int chatType, String time, int isBot) {
        int botType = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");
        Call<JsonObject> res = NetRetrofit
                .getInstance(getApplicationContext())
                .getService()
                .sendMessage(new RequestSendMessage(accountId, content,0, chatType, botType, time, isBot, mJsonResponse));

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e(TAG, String.valueOf(response));
                //TODO : 대화 하다가 앱 종료시 이어서 가능하도록
                if(response.body() != null){
                    Log.e(TAG, String.valueOf(response.body()));

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
}
