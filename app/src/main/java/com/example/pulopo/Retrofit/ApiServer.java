package com.example.pulopo.Retrofit;




import com.example.pulopo.model.response.UserResponse;


import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiServer {
    @POST("User/Login")
    Observable<UserResponse> login(@Query("username") String username,
                                   @Query("password") String password);

}