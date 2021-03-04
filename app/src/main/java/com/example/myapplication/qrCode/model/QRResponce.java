package com.example.myapplication.qrCode.model;


import com.google.gson.annotations.SerializedName;

public  class QRResponce {


    @SerializedName("id")
    public int id;
    @SerializedName("barcode")
    public String barcode;

    @Override
    public String toString() {
        return "QRResponce{" +
                "barcode='" + barcode + '\'' +
                '}';
    }
}