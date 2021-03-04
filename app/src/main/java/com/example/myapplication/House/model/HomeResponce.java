package com.example.myapplication.House.model;


import com.google.gson.annotations.SerializedName;

public  class HomeResponce {


    @SerializedName("qrid")
    public int qrid;

    @SerializedName("cdate")
    public String cdate;

    @SerializedName("ctime")
    public String ctime;

    @Override
    public String toString() {
        return "HomeResponce{" +
                "qrid=" + qrid +
                ", cdate='" + cdate + '\'' +
                ", ctime='" + ctime + '\'' +
                '}';
    }
}