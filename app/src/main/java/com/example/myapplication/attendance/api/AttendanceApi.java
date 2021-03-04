package com.example.myapplication.attendance.api;



import com.example.myapplication.attendance.model.AttendanceResponce;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AttendanceApi {


    @FormUrlEncoded
    @POST("AddAttendance")
    Call<AttendanceResponce> AddAttendance (
            @Field("emp_id") int emp_id,
            @Field("root_id") int root_id,
            @Field("vehicle_id") int vehicle_id
            // lang: eng/mar
    );
}
