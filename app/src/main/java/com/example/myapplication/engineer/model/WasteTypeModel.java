package com.example.myapplication.engineer.model;

public class WasteTypeModel {

    private int  image;
    //private int image_drawable;


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "WasteTypeModel{" +
                "image=" + image +
                '}';
    }
}
