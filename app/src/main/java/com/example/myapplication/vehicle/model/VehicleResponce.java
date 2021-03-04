package com.example.myapplication.vehicle.model;


import com.google.gson.annotations.SerializedName;

public  class VehicleResponce {


    @SerializedName("id")
    public int id;

    @SerializedName("vehicle_type")
    public String vehicle_type;

    @Override
    public String toString() {
        return "RouteResponce{" +
                "id=" + id +
                ", title='" + vehicle_type + '\'' +
                '}';
    }
}