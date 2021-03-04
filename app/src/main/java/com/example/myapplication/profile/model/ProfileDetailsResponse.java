package com.example.myapplication.profile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileDetailsResponse {

    @SerializedName("id")
    public int id;

    @SerializedName("mobile_number")
    public String mobile_number;

    @SerializedName("email")
    public String email;

    @SerializedName("avatar")
    public String avatar;

     @SerializedName("vendor_role_id")
    public String vendor_role_id;

    @SerializedName("first_name")
    public String first_name;

    @SerializedName("middle_name")
    public String middle_name;

    @SerializedName("last_name")
    public String last_name;


    @Override
    public String toString() {
        return "ProfileDetailsResponse{" +
                "id=" + id +
                ", mobile_number='" + mobile_number + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", vendor_role_id='" + vendor_role_id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", middle_name='" + middle_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
