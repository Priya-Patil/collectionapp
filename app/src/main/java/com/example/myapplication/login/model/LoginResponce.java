package com.example.myapplication.login.model;


import com.google.gson.annotations.SerializedName;

public  class LoginResponce {


    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("img")
    public String img;

    @Override
    public String toString() {
        return "LoginResponce{" +
                "id=" + id +
                '}';
    }
}