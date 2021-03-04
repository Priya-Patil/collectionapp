package com.example.myapplication.splash;

import com.google.gson.annotations.SerializedName;

public class AppVersionModel {

    @SerializedName("appversionid")
    public int appversionid;

    @SerializedName("versionno")
    public String versionno;

     @SerializedName("description")
    public String description;

    @SerializedName("createdat")
    public String createdat;


    @Override
    public String toString() {
        return "AppVersionModel{" +
                "appversionid=" + appversionid +
                ", versionno='" + versionno + '\'' +
                ", description='" + description + '\'' +
                ", createdat='" + createdat + '\'' +
                '}';
    }
}
