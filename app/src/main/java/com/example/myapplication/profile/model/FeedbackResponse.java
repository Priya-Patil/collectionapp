package com.example.myapplication.profile.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FeedbackResponse {
    
    @SerializedName("data")
    public ArrayList<FeedbackDetailsResponse> data;

    @SerializedName("content_type")
    public String content_type;

    @SerializedName("message")
    public Object message;

    @SerializedName("status")
    public int status;

    @Override
    public String toString() {
        return "FeedbackResponse{" +
                "data=" + data +
                ", content_type='" + content_type + '\'' +
                ", message=" + message +
                ", status=" + status +
                '}';
    }
}
