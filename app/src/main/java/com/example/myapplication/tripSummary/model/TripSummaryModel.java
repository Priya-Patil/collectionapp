package com.example.myapplication.tripSummary.model;

import com.google.gson.annotations.SerializedName;

public class TripSummaryModel {

    @SerializedName("title")
    public String title;

    @SerializedName("cdate")
    public String cdate;

    @SerializedName("ctime")
    public String ctime;

    @Override
    public String toString() {
        return "TripSummaryModel{" +
                "title=" + title +
                ", cdate='" + cdate + '\'' +
                ", ctime='" + ctime + '\'' +
                '}';
    }
}
