package com.example.myapplication.qrCode.api;



import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.qrCode.model.QRResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QRApi {


    @GET("isQRcodeavailable")
    Call<ArrayList<QRResponce>> isQRcodeavailable (
            @Query("id") String id,
            @Query("status") int status
            // lang: eng/mar

    );
}
