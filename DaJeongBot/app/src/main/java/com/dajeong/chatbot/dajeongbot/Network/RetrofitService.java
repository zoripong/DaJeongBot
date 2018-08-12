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
    @GET("users/{user_id}/{password}")
    Call<ArrayList<JsonObject>> getUserInfo(@Path("user_id") String userId, @Path("password") String password);

    @Headers("Content-Type: application/json")
    @POST("signup")
    Call<JsonObject> addUserInfo(@Body String body);

}
