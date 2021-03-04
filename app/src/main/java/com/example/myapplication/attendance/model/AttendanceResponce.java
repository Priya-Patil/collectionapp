package com.example.myapplication.attendance.model;


import com.google.gson.annotations.SerializedName;

public  class AttendanceResponce {


    @SerializedName("id")
    public int id;

    @Override
    public String toString() {
        return "AttendanceResponce{" +
                "id=" + id +
                '}';
    }
}