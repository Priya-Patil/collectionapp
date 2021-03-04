package com.example.myapplication.wasteCollection.model;


import com.google.gson.annotations.SerializedName;

public  class AddWasteDumpResponce {


    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;

    @Override
    public String toString() {
        return "LoginResponce{" +
                "id=" + id +
                '}';
    }
}