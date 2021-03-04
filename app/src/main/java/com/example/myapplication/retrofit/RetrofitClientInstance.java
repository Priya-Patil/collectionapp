package com.example.myapplication.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit retrofit2;

//     private static final String BASE_URL = "https://onlinekirana.store/cardgame/public/api/";
     private static final String BASE_URL = "http://nagarparishad.avistore.in/api/";
     public static final String IMG_URL = "http://nagarparishad.avistore.in/images/";


    public static Retrofit getRetrofitInstanceServer() {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }

}
