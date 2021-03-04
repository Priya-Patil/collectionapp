package com.example.myapplication.login.model;


import com.google.gson.annotations.SerializedName;

public  class UserDetailsResponce {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("mobile_number")
    public String mobile_number;

    @SerializedName("email")
    public String email;

    @SerializedName("avatar")
    public String avatar;

      @SerializedName("sponsor_code")
    public String sponsor_code;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("updated_at")
    public String updated_at;

    @SerializedName("amount")
    public String amount;

     @SerializedName("txnid")
    public String txnid;

    @Override
    public String toString() {
        return "UserDetailsResponce{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile_number='" + mobile_number + '\'' +
                ", email='" + email + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sponsor_code='" + sponsor_code + '\'' +
                ", status='" + status + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", amount='" + amount + '\'' +
                ", txnid='" + txnid + '\'' +
                '}';
    }
}