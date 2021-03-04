package com.example.myapplication.complaint.api;



import com.example.myapplication.complaint.model.ComplaintResponceModel;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.wasteCollection.model.WasteCollectionModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ComplaintApi {


    @FormUrlEncoded
    @POST("addComplaint")
    Call<StatusResponce> addComplaint (
            @Field("att_id") int att_id,
            @Field("home_id") int home_id,
            @Field("comment") String comment
            // lang: eng/mar

    );


    @GET("getComplaint")
    Call<ArrayList<ComplaintResponceModel>> getComplaint (
            @Query("att_id") int att_id
    );

    @Multipart
    @POST("addWasteCollectionimg")
    Call<StatusResponce> addWasteCollectionimg(
            @Part MultipartBody.Part beforeimg,
            @Part MultipartBody.Part afterimg,
            @Part("collectionid") RequestBody collectionid

    );

}
