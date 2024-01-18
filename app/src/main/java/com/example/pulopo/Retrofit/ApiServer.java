package com.example.pulopo.Retrofit;


import com.example.pulopo.Utils.UserUtil;
import com.example.pulopo.model.response.ChatByUserResponse;
import com.example.pulopo.model.response.ListChatUser;
import com.example.pulopo.model.response.RegisterReponse;
import com.example.pulopo.model.response.SendMessResponse;
import com.example.pulopo.model.response.UserResponse;
import com.google.gson.annotations.SerializedName;


import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServer {


    @POST("User/Login")
    Observable<UserResponse> login(@Body LoginRequest loginRequest);

    @POST("User/Register")
    Observable<RegisterReponse> register(@Query("username") String username,
                                         @Query("password") String password,
                                         @Query("HoTen") String hoTen,
                                         @Query("Email") String email);

    @GET("User/getusers")
    Observable<ListChatUser> getUsers(@Query("rootuserId") int inputId);

    @POST("ChatInfo")
    Observable<SendMessResponse> sendMessChat(@Query("SenderId") int senderId,
                                              @Query("ReceiverId") int recieverId,
                                              @Query("message") String message,
                                              @Query("messageType") int messageType);

    @GET("ChatInfo")
        Observable<ChatByUserResponse> getChatByUser(@Query("SenderId") int senderId,
                                                 @Query("ReceiverId") int recieverId,
                                                 @Query("page") int page);
}