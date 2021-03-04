package com.example.myapplication.tripSummary;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.House.adapter.AddHouseAdapter;
import com.example.myapplication.House.model.HomeResponce;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityTripsummaryBinding;
import com.example.myapplication.databinding.ActivityWastecollectionBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.qrCode.QrAddDumpyard;
import com.example.myapplication.qrCode.QrCodeAddHome;
import com.example.myapplication.qrCode.QrCodeWaste;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.tripSummary.adapter.TripSummaryAdapter;
import com.example.myapplication.tripSummary.api.WasteListApi;
import com.example.myapplication.tripSummary.model.TripSummaryModel;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.zxing.qrcode.encoder.QRCode;


import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripSummaryActivity extends AppCompatActivity  implements View.OnClickListener {
    Activity activity;
    ActivityTripsummaryBinding binding;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private String[] timeList = new String[]{"10 Am","2 pm","3 pm"};
    private String[] dateList = new String[]{"10-1-2021","20-2-2021","25-2-2021"};
    private String[] yardList = new String[]{"21","22","23"};

    TripSummaryAdapter tripSummaruAdapter;
    ArrayList<TripSummaryModel> modelArrayListTripSummary ;
    String type;
    PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tripsummary);
        activity = TripSummaryActivity.this;
        prefManager=new PrefManager(activity);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        listeners();
        getDumpwasteList(prefManager.getUserid(),Utilities.getCurrentDate());
    }
    private void listeners() {
        binding.ivBack.setOnClickListener(this);
        binding.tvSelectdate.setOnClickListener(this);
        binding.imgAdd.setOnClickListener(this);

    }

    private void setDateField() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        binding.tvSelectdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
               // onBackPressed();
                Utilities.launchActivity(this, EngineerHomeActivity.class, true);
                break;

            case R.id.tv_selectdate:
                setDateField();
                break;

            case R.id.img_add:
                Utilities.launchActivity(this, QrAddDumpyard.class, true);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.launchActivity(this, EngineerHomeActivity.class, true);

    }

    void getDumpwasteList(int empid, String created)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            WasteListApi wasteListApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(WasteListApi.class);

            wasteListApi.getDumpwasteList(empid, created).
                    enqueue(new Callback<ArrayList<TripSummaryModel>>() {

                        @Override
                        public void onResponse(Call<ArrayList<TripSummaryModel>> call, Response<ArrayList<TripSummaryModel>> response) {

                            ArrayList<TripSummaryModel> bettingResponce = response.body();
                            Log.e( "onResponse: ", bettingResponce.toString() );
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            if(bettingResponce.size()==0)
                            {
                                Utilities.messageDialog(activity,"No record");
                            }
                            else {
                                setWalletHistory(bettingResponce);
                            }

                        }

                        @Override
                        public void onFailure(Call <ArrayList<TripSummaryModel>> call, Throwable t) {
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.e("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }


    void setWalletHistory(ArrayList<TripSummaryModel> homeResponces)
    {
        binding.rvTripSummary.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvTripSummary.setItemAnimator(new DefaultItemAnimator());
        binding.rvTripSummary.setHasFixedSize(true);
        tripSummaruAdapter = new TripSummaryAdapter(activity,homeResponces);

        binding.rvTripSummary.setAdapter(tripSummaruAdapter);
    }


}