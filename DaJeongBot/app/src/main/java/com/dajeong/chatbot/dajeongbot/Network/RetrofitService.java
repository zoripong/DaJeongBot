package com.dajeong.chatbot.dajeongbot.Network;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {
    @GET("users/{account_type}/{user_id}/{password}")
    Call<ArrayList<JsonObject>> getUserInfo(@Path("account_type")int accountType, @Path("user_id") String userId, @Path("password") String password);

    @Headers("Content-Type: application/json")
    @POST("signup")
    Call<JsonObject> addUserInfo(@Body String body);

    @GET("messages/{account_id}")
    Call<ArrayList<JsonObject>> getMessages(@Path("account_id") int accountId);

    @GET("messages/{account_id}/{last_index}")
    Call<ArrayList<JsonObject>> getMessages(@Path("account_id") int accountId, @Path("last_index") int lastIndex);


    @Headers("Content-Type: application/json")
    @POST("messages")
    Call<JsonObject> sendMessage(@Body String body);



}
