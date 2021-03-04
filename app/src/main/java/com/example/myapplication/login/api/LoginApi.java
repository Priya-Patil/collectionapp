package com.example.myapplication.login.api;



import com.example.myapplication.login.model.LoginResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginApi {


   // http://api.eurekatalents.in/api/customer/login
  /* mobile_number9823017720
           password123456
*/
    @FormUrlEncoded
    @POST("getEmpLogin")
    Call<ArrayList<LoginResponce>> checkLogin (
            @Field("username") String username,
            @Field("password") String password
            // lang: eng/mar

    );
}
