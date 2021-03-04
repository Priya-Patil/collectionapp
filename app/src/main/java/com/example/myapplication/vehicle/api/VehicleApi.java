package com.example.myapplication.vehicle.api;



import com.example.myapplication.vehicle.model.VehicleResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VehicleApi {


    @GET("getVehicles")
    Call<ArrayList<VehicleResponce>> getVehicles (

    );
}
