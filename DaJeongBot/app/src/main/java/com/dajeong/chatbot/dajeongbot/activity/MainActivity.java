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
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.adapter.ChatAdapter;
import com.dajeong.chatbot.dajeongbot.alias.ChatType;
import com.dajeong.chatbot.dajeongbot.alias.NodeType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.Character;
import com.dajeong.chatbot.dajeongbot.model.Chat;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSendMessage;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// TODO : 중간에 네트워크 연결되었을 경우
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

    private JsonObject mJsonResponse;
    private HashMap<String, Integer> mStringNodeTypeMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getMessage();

        showProgressBar();

        test();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRvChatList.setHasFixedSize(true);
        mRvChatList.setLayoutManager(layoutManager);

        mRvChatList.setAdapter(mChatAdapter);

        mRvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mRvChatList.canScrollVertically(-1)) {
                    Log.e(TAG, "OnScrolled : TOP");
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
                    int chatType = ChatType.BASIC_CHAT;
                    long time = System.currentTimeMillis();
                    int isBot = 0;

                    sendMessage(accountId, content, chatType, String.valueOf(time), isBot);

                    mChats.add(new Chat(NodeType.SPEAK_NODE, null, mEtMessage.getText().toString(), String.valueOf(System.currentTimeMillis())));
                    mChatAdapter.notifyDataSetChanged();
                    mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    mEtMessage.setText("");
                }

            }


        });

        findViewById(R.id.ivAddChat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.ivAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivity(intent);
            }
        });
    }

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
//        mAccountId = Integer.parseInt(CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getStringPreferences("id"));
        mIsLoad = false;
        mMoreChat = true;
        mChatType = ChatType.BASIC_CHAT;

        mJsonResponse = new JsonObject();
        mStringNodeTypeMap = new HashMap<>();
        mStringNodeTypeMap.put("speak", NodeType.SPEAK_NODE);
        mStringNodeTypeMap.put("slot", NodeType.SLOT_NODE);
        mStringNodeTypeMap.put("carousel", NodeType.CAROUSEL_NODE);
    }

    @NonNull
    private Character setBot(){
        int charImage = R.drawable.ic_char1;
        String charNames[] = {"다정군", "다정냥", "다정곰", "다정몽"};
        int charType = CustomSharedPreference.getInstance(getApplicationContext(), "user_info").getIntPreferences("bot_type");

        return new Character(charNames[charType=1], charImage+charType);
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

    // 테스트할려면 getMoreMessage 주석처리, getMessage 주석처리 해서 테스트
    private void getMoreMessage(){
        int lastIndex = CustomSharedPreference.getInstance(getApplicationContext(), "chat").getIntPreferences("last_index");

        Log.e(TAG, "Last Index is "+ lastIndex);

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

        Log.e(TAG, "HERE"+String.valueOf(mAccountId));

        ArrayList<JsonObject> body = response.body();

        for(JsonObject json : body){
            if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 0)
                mChats.addFirst(new Chat(json.get("chat_type").getAsInt(), null, json.get("content").getAsString(), json.get("time").getAsString()));
            else if(Integer.parseInt(String.valueOf(json.get("isBot"))) == 1)
                mChats.addFirst(new Chat(json.get("chat_type").getAsInt(), mBotChar,json.get("content").getAsString(), json.get("time").getAsString()));

//            Log.e(TAG, "얍"+String.valueOf(json.get("content"))+"/"+mChats.size());

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

        Call<JsonObject> res = NetRetrofit
                .getInstance(getApplicationContext())
                .getService()
                .sendMessage(new RequestSendMessage(accountId, content,0, chatType, time, isBot, mJsonResponse));

        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, String.valueOf(response));
                //TODO : 대화 하다가 앱 종료시 이어서 가능하도록
                if(response.body() != null){
                    Log.e(TAG, String.valueOf(response.body()
                    ));
                    if(response.body().has("status")){
                        if(response.body().get("status").getAsString().equals("Failed")){
                            Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하였습니다. 다시 시도해주세요.", Toast.LENGTH_LONG).show();
                        }

                    }else if(response.body().has("responseSet")){
                    mChatType = ChatType.BASIC_CHAT;
                    // 챗봇과 대화한 경우
                    Log.e(TAG, response.body().getAsJsonObject("responseSet").getAsJsonObject("result").toString());
                        mJsonResponse = response.body().getAsJsonObject("responseSet").getAsJsonObject("result");
                    JsonArray jsonArray = mJsonResponse.getAsJsonArray( "result");
                    for( int i = 0; i<jsonArray.size(); i++ ){

                        String message = jsonArray.get(i).getAsJsonObject().get("message").getAsString();
                        String timestamp = String.valueOf(jsonArray.get(i).getAsJsonObject().get("timestamp").getAsLong());
                        String nodeType = jsonArray.get(i).getAsJsonObject().get("nodeType").getAsString();
                        Log.e(TAG, "node type is " + nodeType);
                        JsonArray options = jsonArray.get(i).getAsJsonObject().getAsJsonArray("optionList");

                        if(options.size() > 0){
                            //TODO : EditText enable
                                mChats.addLast(new Chat(NodeType.SLOT_NODE, mBotChar, message, timestamp, options));
                        }else{
                            mChats.addLast(new Chat(NodeType.SPEAK_NODE, mBotChar, message, timestamp));
                        }

                    }
                    mChatAdapter.notifyDataSetChanged();
                    mRvChatList.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }else if(response.body().getAsJsonObject("result").has("events")){
                        // 추억 리스트 보여주기
                    //TODO: 버튼 클릭할 때 request 정보
                    // carousel item
                    JsonObject result = response.body().getAsJsonObject("result");

                    String timestamp = String.valueOf(result.get("time").getAsLong());

                    JsonArray messages = result.getAsJsonArray("content");
                    for(int i = 0; i<messages.size(); i++){
                        mChats.addFirst(new Chat(0, mBotChar, messages.get(i).getAsString(), timestamp));
                    }

                    JsonArray events = result.getAsJsonArray("events");
                    for(int i = 0; i<events.size(); i++){

                    }

                }
//                    mChats.addLast(new Chat(mStringNodeTypeMap.get(nodeType), mBotChar, message, timestamp));
                }else{
                    Log.e(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    Log.e(TAG, t.toString());
                }
            }
        });

    }

    public void sendSlotMessage(){
        Toast.makeText(getApplicationContext(), "slot", Toast.LENGTH_LONG).show();
    }

    private void showProgressBar(){
        findViewById(R.id.pgb).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar(){
        findViewById(R.id.pgb).setVisibility(View.INVISIBLE);
    }

    private void test(){
        hideProgressBar();
        mChats.addFirst(new Chat(NodeType.CAROUSEL_NODE, mBotChar, "골라봐!", String.valueOf(System.currentTimeMillis())));
    }

}
