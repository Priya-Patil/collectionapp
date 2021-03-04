package com.example.myapplication.wasteCollection.model;

import com.google.gson.annotations.SerializedName;

public class WasteCollectionModel {

    @SerializedName("home_id")
    public int home_id;

    @SerializedName("cdate")
    public String cdate;

    @SerializedName("ctime")
    public String ctime;

    @Override
    public String toString() {
        return "TripSummaryModel{" +
                "home_id=" + home_id +
                ", cdate='" + cdate + '\'' +
                ", ctime='" + ctime + '\'' +
                '}';
    }
}
