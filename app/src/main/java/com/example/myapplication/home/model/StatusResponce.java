package com.example.myapplication.home.model;


import com.google.gson.annotations.SerializedName;

public  class StatusResponce {


    @SerializedName("status")
    public String status;

    @Override
    public String toString() {
        return "StatusResponce{" +
                "status='" + status + '\'' +
                '}';
    }
}