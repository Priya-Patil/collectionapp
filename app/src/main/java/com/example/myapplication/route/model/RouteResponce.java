package com.example.myapplication.route.model;


import com.google.gson.annotations.SerializedName;

public  class RouteResponce {


    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @Override
    public String toString() {
        return "RouteResponce{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}