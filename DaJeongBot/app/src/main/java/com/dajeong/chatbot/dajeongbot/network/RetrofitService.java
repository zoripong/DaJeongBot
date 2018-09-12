package com.dajeong.chatbot.dajeongbot.network;

import com.dajeong.chatbot.dajeongbot.model.request.RequestRegisterToken;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSendMessage;
import com.dajeong.chatbot.dajeongbot.model.request.RequestSignUp;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateBot;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateName;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateTime;
import com.dajeong.chatbot.dajeongbot.model.request.RequestUpdateToken;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {
    // check internet status
    @GET("/")
    Call<JsonObject> checkInternetStatus();

    // 로그인 & 회원가입
    @GET("users/{account_type}/{user_id}/{password}")
    Call<ArrayList<JsonObject>> getUserInfo(@Path("account_type")int accountType, @Path("user_id") String userId, @Path("password") String password);

    @Headers("Content-Type: application/json")
    @POST("users/signup")
    Call<JsonObject> addUserInfo(@Body RequestSignUp params);

    // 채팅
    @GET("messages/welcome/{account_id}")
    Call<JsonObject> getMessagesForNewUser(@Path("account_id") int accountId);

    @GET("messages/{account_id}")
    Call<ArrayList<JsonObject>> getMessages(@Path("account_id") int accountId);

    @GET("messages/{account_id}/{last_index}")
    Call<ArrayList<JsonObject>> getMessages(@Path("account_id") int accountId, @Path("last_index") int lastIndex);

    @Headers("Content-Type: application/json")
    @POST("messages/")
    Call<JsonObject> sendMessage(@Body RequestSendMessage params);

    // 일정
    @GET("events/{account_id}/{year}/{month}/{date}")
    Call<ArrayList<JsonObject>> getEvent(@Path("account_id") int accountId, @Path("year") String year, @Path("month") String month, @Path("date") String date);

    @GET("events/dates/{account_id}")
    Call<ArrayList<String>> getDatesHavingEvent(@Path("account_id") int accountId);

    // fcm 토큰 등록
    @Headers("Content-Type: application/json")
    @POST("me/tokens")
    Call<JsonObject> registerFcmToken(@Body RequestRegisterToken params);

    // fcm 토큰 업데이트
    @PUT("me/tokens/")
    Call<JsonObject> updateFcmToken(@Body RequestUpdateToken params);

    // fcm 토큰 삭제
    @DELETE("me/tokens/{account_id}/{fcm_token}")
    Call<JsonObject> releaseFcmToken(@Path("account_id")int accountId, @Path("fcm_token")String fcmToken);

    // 사용자 닉네임 업데이트
    @PUT("me/names/")
    Call<JsonObject> updateName(@Body RequestUpdateName params);

    // 챗봇 업데이트
    @PUT("me/bots/")
    Call<JsonObject> updateBots(@Body RequestUpdateBot params);

    // 알림 시간 업데이트
    @PUT("me/times/")
    Call<JsonObject> updateTimes(@Body RequestUpdateTime params);





}
