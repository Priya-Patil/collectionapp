package com.example.myapplication.otp.api;


import com.example.myapplication.otp.model.OTPResponce;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface OTPApi {


    @FormUrlEncoded
    @POST("saveUser")
    Call<OTPResponce> checkLogin (
            @Field("mobile") String mobile
            // lang: eng/mar

    );
}
