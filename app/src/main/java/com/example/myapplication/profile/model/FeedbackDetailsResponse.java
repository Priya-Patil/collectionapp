package com.example.myapplication.profile.model;

import com.google.gson.annotations.SerializedName;

public class FeedbackDetailsResponse {
    
    @SerializedName("id")
    public int id;

    @SerializedName("vendor_user_id")
    public String vendor_user_id;

    @SerializedName("title")
    public String title;

     @SerializedName("description")
    public String description;

     @SerializedName("feedback")
    public String feedback;

     @SerializedName("request_type")
    public String request_type;

     @SerializedName("status")
    public String status;

     @SerializedName("created_at")
    public String created_at;

     @SerializedName("updated_at")
    public String updated_at;

    @Override
    public String toString() {
        return "FeedbackDetailsResponse{" +
                "id=" + id +
                ", vendor_user_id='" + vendor_user_id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", feedback='" + feedback + '\'' +
                ", request_type='" + request_type + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
