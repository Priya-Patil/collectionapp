package com.example.myapplication.route.api;



import com.example.myapplication.route.model.RouteResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RouteApi {


    @GET("getRoute")
    Call<ArrayList<RouteResponce>> getRoute (

    );
}
