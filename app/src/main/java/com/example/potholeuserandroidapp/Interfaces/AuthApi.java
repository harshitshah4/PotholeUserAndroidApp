package com.example.potholeuserandroidapp.Interfaces;

import com.example.potholeuserandroidapp.Models.ResponseBody;
import com.example.potholeuserandroidapp.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("auth/getsignupotp")
    Call<ResponseBody> getSignupOtp(@Body User user);

    @POST("auth/signup")
    Call<ResponseBody> signup(@Body User user);

    @POST("auth/login")
    Call<ResponseBody> login(@Body User user);



}
