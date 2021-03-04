package com.example.myapplication.profile.model;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    
    @SerializedName("data")
    public ProfileDetailsResponse data;

    @SerializedName("content_type")
    public String content_type;

    @SerializedName("message")
    public String message;

    @SerializedName("status")
    public int status;


    @Override
    public String toString() {
        return "ProfileResponse{" +
                "data=" + data +
                ", content_type='" + content_type + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
