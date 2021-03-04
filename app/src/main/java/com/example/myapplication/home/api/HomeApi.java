package com.example.myapplication.home.api;



import com.example.myapplication.House.model.HomeResponce;
import com.example.myapplication.home.model.StatusResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HomeApi {

    @FormUrlEncoded
    @POST("AddHouse")
    Call<StatusResponce> AddHouse (
            @Field("ward_id") String ward_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("name") String name,
            @Field("mobile") String mobile,
            @Field("address") String address,
            @Field("family_members") String family_members,
            @Field("waste_id") String waste_id,
            @Field("empid") int empid,
            @Field("qrid") int qrid,
            @Field("routeid") int routeid
            // lang: eng/mar

    );

    @GET("getHouseList")
    Call<ArrayList<HomeResponce>> getHouseList (
            @Query("empid") int empid


    );
}
