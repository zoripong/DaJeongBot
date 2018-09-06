package com.dajeong.chatbot.dajeongbot.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.alias.AccountType;
import com.dajeong.chatbot.dajeongbot.control.CustomSharedPreference;
import com.dajeong.chatbot.dajeongbot.model.request.RequestRegisterToken;
import com.dajeong.chatbot.dajeongbot.network.NetRetrofit;
import com.dajeong.chatbot.dajeongbot.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.usage.NetworkStats.Bucket.STATE_DEFAULT;
import static com.kakao.usermgmt.StringSet.email;


// 로그인
public class LoginActivity extends AppCompatActivity {
    // control ui variables
    private EditText mEtUserId;
    private EditText mEtUserPw;
    private Button mBtnFacebookLogIn;

    // api 로그인
    private SessionCallback callback;      //콜백 선언
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;

    // TAG variables for Log
    private static final String TAG = "LoginActivity";
    private static final String FACEBOOK_TAG = "Facebook";
    private static final String GOOGLE_TAG = "google";
    private static final String KAKAO_TAG = "kakao";

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행
        setContentView(R.layout.activity_login);
        init();

        //구글
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        //facebook start
        mFacebookCallbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자

        //반면에 커스텀으로 만든 버튼을 사용할 경우 아래보면 CustomloginButton OnClickListener안에 LoginManager를 이용해서
        //로그인 처리를 해주어야 합니다.
        mBtnFacebookLogIn = (Button)findViewById(R.id.btn_facebook_login);
        mBtnFacebookLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends","email"));
                LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {


                                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                                Log.e("TAG", "페이스북 토큰 -> " + loginResult.getAccessToken().getToken());
                                Log.e("onSuccess", "onSuccess77");
                                Log.d("Success", String.valueOf(loginResult.getAccessToken()));
                                //    Log.d("Success", String.valueOf(Profile.getCurrentProfile().getId()));
                                //    Log.d("Success", String.valueOf(Profile.getCurrentProfile().getName()));
                                //   Log.d("Success", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));
                                requestUserProfile(loginResult);
                                redirectSignUpActivity(Profile.getCurrentProfile().getId(), "", AccountType.FACEBOOK_ACCOUNT);  // 세션 연결성공 시 redirectSignUpActivity() 호출
                            }

                            @Override
                            public void onCancel() {
                                Log.e("onCancel", "onCancel");
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                Log.e("onError", "onError " + exception.getLocalizedMessage());
                            }
                        });
            }
        });
        //facebook end


        // 로그인 버튼 click
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(mEtUserId.getText());
                String pw = String.valueOf(mEtUserPw.getText());
                // TODO: Remove the test code
//                id = "test";
                pw = "test";
                if((!id.isEmpty())&&(!pw.isEmpty())){
                    findViewById(R.id.pgb).setVisibility(View.VISIBLE);
                    Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getUserInfo(AccountType.BASIC_ACCOUNT, id, pw);
                        new NetworkCall().execute(res);
                }else{
                    Toast.makeText(getApplicationContext(), "입력해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });

        findViewById(R.id.btnSignin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 0 : basic
                * 1 : facebook
                * 2 : kakao
                * 3 : google
                * */
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.putExtra("account_type", 0);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.btn_kakao_login).setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                Session session = Session.getCurrentSession();
                session.addCallback(new SessionCallback());
                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }

        });

    }

    private void init() {
        mEtUserId = findViewById(R.id.etUserId);
        mEtUserPw = findViewById(R.id.etUserPw);
    }


    // FIXME : token 여러개 들어가는거..ㅠㅠ
    private class NetworkCall extends AsyncTask<Call, Void, String> {
        @Override
        protected String doInBackground(Call... calls) {
            try {
                Call<List<JsonObject>> call = calls[0];
                Response<List<JsonObject>> response = call.execute();
                return response.body().toString();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            findViewById(R.id.pgb).setVisibility(View.INVISIBLE);

            if(result != null)
                Log.e(TAG, result);

            try {
                JSONObject response = new JSONArray(result).getJSONObject(0);
                if(response.getString("status").equals("OK")){
                    saveUserInfo(response.getJSONObject("user_info"));

                }else if(response.getString("status").equals("NOT EXIST")){
                    Toast.makeText(getApplicationContext(), "일치하는 회원이 없습니다.", Toast.LENGTH_SHORT).show();
                }else if(response.getString("status").equals("NEW_API_USER")){

                    Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                    intent.putExtra("account_type", response.getInt("account_type"));
                    intent.putExtra("token", response.getString("token"));
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(getApplicationContext(), "오류가 발생하였습니다. 개발자에게 문의해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    //facebook start
    public void requestUserProfile(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d(FACEBOOK_TAG, "페이스북 사용자 아이디 : "+response.getJSONObject().getString("id"));
                            String email = response.getJSONObject().getString("email").toString();
                            Log.d(FACEBOOK_TAG, "페이스북 사용자 이메일 : "+email);
                            Log.d(FACEBOOK_TAG, "페이스북 사용자 이름 : "+response.getJSONObject().getString("name"));
                            Log.d(FACEBOOK_TAG, "페이스북 사용자 사진 : "+response.getJSONObject().getString("picture"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    //facebook end///

    //google LogIn start
    public void mOnClick(View view){
        switch (view.getId()){
            case R.id.btn_google_login:
                signIn();
                break;
        }
    }

    private void signIn(){
        Intent signInIntent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //구글
        if(mGoogleApiClient!=null) {
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
        else{
            Log.e(TAG,"mGoogleApiClient is NULL");
        }
        //페이스북
        if(mFacebookCallbackManager!=null) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }
        else {
            Log.e(TAG,"mGoogleApiClient is NULL");
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult : " + result.isSuccess());
        if(result.isSuccess()){ //구글 로그인 성공
            GoogleSignInAccount acct=result.getSignInAccount();
            String authCode=acct.getServerAuthCode();
            String googleToken=acct.getIdToken();
            String googleName=acct.getDisplayName();
            String googleEmail=acct.getEmail();
            String googleID=acct.getId();
            Uri googlePhoto=acct.getPhotoUrl();

            Log.e(GOOGLE_TAG,"구글 사용자 Authcode"+authCode);
            Log.e(GOOGLE_TAG,"구글 사용자 토큰"+googleToken);
            Log.e(GOOGLE_TAG,"구글 사용자 이름"+googleName);
            Log.e(GOOGLE_TAG,"구글 사용자 이메일"+googleEmail);
            Log.e(GOOGLE_TAG,"구글 사용자 ID"+googleID);
            Log.e(GOOGLE_TAG,"구글 사용자 포토"+googlePhoto);

            redirectSignUpActivity(acct.getId(), "", AccountType.GOOGLE_ACCOUNT);  // 세션 연결성공 시 redirectSignUpActivity() 호출
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    public class SessionCallback implements ISessionCallback {
        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }
        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }
        // 사용자 정보 요청
        public void requestMe() {
            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.getInstance().requestMe(new MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }
                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp");
                }
                // 사용자정보 요청에 성공한 경우,
                @Override
                public void onSuccess(UserProfile userProfile) {
                    Log.e("SessionCallback :: ", "onSuccess");
                    String nickname = userProfile.getNickname();
                    String email = userProfile.getEmail();
                    String profileImagePath = userProfile.getProfileImagePath();
                    String thumbnailImage = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    long id = userProfile.getId();

                    String accessToken = Session.getCurrentSession().getAccessToken();
                    Log.e(KAKAO_TAG, "카카오톡 사용자 토큰 : "+accessToken);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 이름 : "+nickname);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 이메일 : "+email);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 profileImagePath : "+profileImagePath);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 thumbnailPath : "+thumbnailImage);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 UUID : "+UUID);
                    Log.e(KAKAO_TAG, "카카오톡 사용자 아이디 : "+id);
                    redirectSignUpActivity(email, "token", AccountType.KAKAO_ACCOUNT);
                }



                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
                }
            });
        }
    }
    //kakao LogIn end

    protected void redirectSignUpActivity(String id, String token, int accountType) {       //세션 연결 성공 시 SignupActivity로 넘김
        id = "test";
        token = "test";

        if((!id.isEmpty())&&(!token.isEmpty())){
            findViewById(R.id.pgb).setVisibility(View.VISIBLE);
            Call<ArrayList<JsonObject>> res = NetRetrofit.getInstance(getApplicationContext()).getService().getUserInfo(accountType, id, token);
            new NetworkCall().execute(res);
        }else{
            Toast.makeText(getApplicationContext(), "입력해주세요.", Toast.LENGTH_LONG).show();
        }

        /*x
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        * */
    }

    private void saveUserInfo(final JSONObject userJson) throws JSONException {
        //Todo : test

        // 로그인 정보 저장
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        final RequestRegisterToken param = new RequestRegisterToken(Integer.parseInt(userJson.getString("id")), refreshedToken);
        Call<JsonObject> res = NetRetrofit.getInstance(getApplicationContext()).getService().registerFcmToken(param);
        res.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.body().has("status")){
                    if(response.body().get("status").getAsString().equals("Success")){
                        Log.i(TAG, "새로운 토큰 등록에 성공하였습니다.\n"+ param.toString());
                        CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("fcm_token", refreshedToken);
                        try {
                            CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("id", userJson.getString("id"));
                            CustomSharedPreference.getInstance(getApplicationContext(), "user_info").savePreferences("bot_type", userJson.getInt("bot_type"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Log.e(TAG, "서버의 문제로 토큰 등록에 실패하였습니다.");
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                if(t!=null){
                    Toast.makeText(getApplicationContext(), "네트워크에 문제가 발생하여 해당 기기 토큰을 저장하지 못하였습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }


}


