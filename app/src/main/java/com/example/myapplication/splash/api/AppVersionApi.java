package com.example.myapplication.splash.api;



import com.example.myapplication.splash.AppVersionModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AppVersionApi {


    @GET("getAppVersion")
    Call<ArrayList<AppVersionModel>> getVersion();



}
