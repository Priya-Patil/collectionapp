package com.example.myapplication.profile.api;




import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.profile.model.ProfileImageUpdateResponse;
import com.example.myapplication.profile.model.ProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface ProfileApi {


    @FormUrlEncoded
    @POST("vendor/profile")
    Call  <ProfileResponse> getProfile (
            @Field("user_id") int user_id
            );

    @Multipart
    @POST("addWasteCollectionimg")
    Call<StatusResponce> addWasteCollectionimg(
                        @Part MultipartBody.Part beforeimg,
                        @Part MultipartBody.Part afterimg,
                        @Part("collectionid") RequestBody collectionid

                        );

    /*
    @Multipart
    @POST("vendor/image-update")
    Call<ProfileImageUpdateResponse> updateProfileImage(
                        @Part("id") RequestBody id,
                        @Part MultipartBody.Part file
    );*/

    @FormUrlEncoded
    @POST("vendor/profile-update")
    Call  <ProfileResponse> updateProfile (
            @Field("user_id") int user_id,
            @Field("first_name") String first_name,
            @Field("middle_name") String middle_name,
            @Field("last_name") String last_name,
            @Field("mobile_number") String mobile_number,
            @Field("email") String email
    );

}
