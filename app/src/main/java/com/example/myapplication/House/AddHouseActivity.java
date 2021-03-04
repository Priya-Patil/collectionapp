package com.example.myapplication.House;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.location.LocationManager;
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

import com.example.myapplication.House.model.HomeResponce;
import com.example.myapplication.R;
import com.example.myapplication.House.adapter.AddHouseAdapter;
import com.example.myapplication.House.model.AddHouseModel;
import com.example.myapplication.databinding.ActivityAddhouseBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.qrCode.QrCodeAddHome;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.facebook.shimmer.ShimmerFrameLayout;


import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddHouseActivity extends AppCompatActivity  implements View.OnClickListener {
    Activity activity;
    ActivityAddhouseBinding binding;
    
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
     AddHouseAdapter addHouseAdapter;
    PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_addhouse);
        activity = AddHouseActivity.this;
        prefManager=new PrefManager(activity);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        listeners();
        getHouseList(prefManager.getUserid());
    }
    private void listeners() {
        binding.ivBack.setOnClickListener(this);
        binding.tvSelectdate.setOnClickListener(this);
        binding.imgAdd.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);

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

            case R.id.btn_search:
               // onBackPressed();

                Toast.makeText(activity, "coming soon", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_selectdate:
                setDateField();
                break;
            case R.id.img_add:

                LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
                boolean gps_enabled = false;
                boolean network_enabled = false;

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                } catch(Exception ex) {}

                if(!gps_enabled && !network_enabled) {
                    // notify user
                    Utilities.messageDialog(activity,"Please enable GPS!...");
                }
                //enabled
                else {
                    Utilities.launchActivity(activity, QrCodeAddHome.class,false);
                }
                break;

        }

    }


    void getHouseList(int uid)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            HomeApi homeApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(HomeApi.class);

            homeApi.getHouseList(uid).
                    enqueue(new Callback<ArrayList<HomeResponce>>() {

                        @Override
                        public void onResponse(Call<ArrayList<HomeResponce>> call, Response<ArrayList<HomeResponce>> response) {

                            ArrayList<HomeResponce> bettingResponce = response.body();

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
                        public void onFailure(Call <ArrayList<HomeResponce>> call, Throwable t) {

                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.e("errorchk",t.getMessage());

                        }
                    }
                    );
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }


    void setWalletHistory(ArrayList<HomeResponce> homeResponces)
    {
        binding.rvAddhouse.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvAddhouse.setItemAnimator(new DefaultItemAnimator());
        binding.rvAddhouse.setHasFixedSize(true);
        addHouseAdapter = new AddHouseAdapter(activity,homeResponces);

        binding.rvAddhouse.setAdapter(addHouseAdapter);
    }



}