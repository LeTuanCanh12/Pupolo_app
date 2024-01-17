package com.example.pulopo.Retrofit;




import com.example.pulopo.model.response.RegisterReponse;
import com.example.pulopo.model.response.UserResponse;


import io.reactivex.rxjava3.core.Observable;

import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServer {
    @POST("User/Login")
    Observable<UserResponse> login(@Query("username") String username,
                                   @Query("password") String password);

    @POST("User/Register")
    Observable<RegisterReponse> register(@Query("username") String username,
                                         @Query("password") String password,
                                         @Query("HoTen") String hoTen,
                                         @Query("Email") String email);

}