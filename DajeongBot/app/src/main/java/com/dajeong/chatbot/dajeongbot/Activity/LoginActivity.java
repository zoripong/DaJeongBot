package com.dajeong.chatbot.dajeongbot.Activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dajeong.chatbot.dajeongbot.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


// 로그인
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    SessionCallback callback;      //콜백 선언

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mFacebookCallbackManager;
    private Button facebookLoginButton;
    private Button btn_custom_login;
    private static final String FACEBOOKTAG = "Facebook";
    private static final String GOOGLETAG = "google";
    private static final String KAKAOTAG = "kakao";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //<< this
        FacebookSdk.sdkInitialize(getApplicationContext()); // SDK 초기화 (setContentView 보다 먼저 실행
        setContentView(R.layout.activity_login);

        //facebook start
        mFacebookCallbackManager = CallbackManager.Factory.create();  //로그인 응답을 처리할 콜백 관리자

        //반면에 커스텀으로 만든 버튼을 사용할 경우 아래보면 CustomloginButton OnClickListener안에 LoginManager를 이용해서
        //로그인 처리를 해주어야 합니다.
        facebookLoginButton = (Button)findViewById(R.id.btn_facebook_login);
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LoginManager - 요청된 읽기 또는 게시 권한으로 로그인 절차를 시작합니다.
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends","email"));
                LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                        new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                Log.e("onSuccess", "onSuccess");
                                Log.d("Success", String.valueOf(loginResult.getAccessToken()));
                                //    Log.d("Success", String.valueOf(Profile.getCurrentProfile().getId()));
                                //    Log.d("Success", String.valueOf(Profile.getCurrentProfile().getName()));
                                //   Log.d("Success", String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(200, 200)));
                                requestUserProfile(loginResult);
                                redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
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


        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
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
    //facebook start
    public void requestUserProfile(LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Log.d(FACEBOOKTAG, "페이스북 사용자 아이디 : "+response.getJSONObject().getString("id"));
                            String email = response.getJSONObject().getString("email").toString();
                            Log.d(FACEBOOKTAG, "페이스북 사용자 이메일 : "+email);
                            Log.d(FACEBOOKTAG, "페이스북 사용자 이름 : "+response.getJSONObject().getString("name"));
                            Log.d(FACEBOOKTAG, "페이스북 사용자 사진 : "+response.getJSONObject().getString("picture"));

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
                Toast.makeText(this, "접속합니다", Toast.LENGTH_SHORT).show();

                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(Plus.API)
                        .addScope(Plus.SCOPE_PLUS_PROFILE)
                        .build();

                mGoogleApiClient.connect();

                break;
        }
    }



    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "구글 플레이 연결이 되었습니다.");

        if (!mGoogleApiClient.isConnected() || Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) == null) {

            Log.d(TAG, "onConnected 연결 실패");

        } else {
            Log.d(TAG, "onConnected 연결 성공");

            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            if (currentPerson.hasImage()) {

                Log.d(TAG, "이미지 경로는 : " + currentPerson.getImage().getUrl());

               /* Glide.with(MainActivity.this)
                        .load(currentPerson.getImage().getUrl())
                        .into(userphoto);*/

            }
            if (currentPerson.hasDisplayName()) {
                Log.d(GOOGLETAG,"구글 사용자 이름 : "+ currentPerson.getDisplayName());
                Log.d(GOOGLETAG, "구글 사용자 아이디 : " + currentPerson.getId());
                Log.d(GOOGLETAG, "구글 사용자 성별 : " + currentPerson.getGender());
                Log.d(GOOGLETAG, "구글 사용자 생일 : " + currentPerson.getBirthday());
//                String GoogleEmail = Plus.AccountApi.getAccountName(mGoogleApiClient); //이메일만 오류남
//                Log.d(TAG, "구글 사용자 이메일 : " + GoogleEmail);

            }
            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "연결 에러 " + connectionResult);

        if (connectionResult.hasResolution()) {

            Log.e(TAG,
                    String.format(
                            "Connection to Play Services Failed, error: %d, reason: %s",
                            connectionResult.getErrorCode(),
                            connectionResult.toString()));
            try {
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, e.toString(), e);
            }
        }else{
            Toast.makeText(getApplicationContext(), "이미 로그인 중", Toast.LENGTH_SHORT).show();
        }
    }

    //google LogIn end

    //kakao LogIn start
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
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
                    String thumnailPath = userProfile.getThumbnailImagePath();
                    String UUID = userProfile.getUUID();
                    long id = userProfile.getId();

                    Log.e(KAKAOTAG, "카카오톡 사용자 이름 : "+nickname);
                    Log.e(KAKAOTAG, "카카오톡 사용자 이메일 : "+email);
                    Log.e(KAKAOTAG, "카카오톡 사용자 profileImagePath : "+profileImagePath);
                    Log.e(KAKAOTAG, "카카오톡 사용자 thumnailPath : "+thumnailPath);
                    Log.e(KAKAOTAG, "카카오톡 사용자 UUID : "+UUID);
                    Log.e(KAKAOTAG, "카카오톡 사용자 아이디 : "+id);
                    redirectSignupActivity();
                }



                // 사용자 정보 요청 실패
                @Override
                public void onFailure(ErrorResult errorResult) {

                    Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());

                }

            });

        }

    }

    protected void redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
    //kakao LogIn end
}


