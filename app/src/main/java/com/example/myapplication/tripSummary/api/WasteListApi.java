package com.example.myapplication.tripSummary.api;



import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.route.model.RouteResponce;
import com.example.myapplication.tripSummary.model.TripSummaryModel;
import com.example.myapplication.wasteCollection.model.WasteCollectionModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface WasteListApi {


    @GET("getWasteList")
    Call<ArrayList<WasteCollectionModel>> getWasteList (
            @Query("att_id") int att_id
    );
    @GET("getDumpwasteList")
    Call<ArrayList<TripSummaryModel>> getDumpwasteList (
            @Query("empid") int empid,
            @Query("created") String created
    );


    @FormUrlEncoded
    @POST("addDumpwaste")
    Call<StatusResponce> addDumpwaste (
            @Field("empid") int empid,
            @Field("dumpyardid") int dumpyardid,
            @Field("drywaste") String drywaste,
            @Field("wetwaste") String wetwaste,
            @Field("mixwaste") String mixwaste

    );
}
