package com.example.myapplication.engineer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityAddRouteOrHouseBinding;
import com.example.myapplication.map.GpsTracker;
import com.example.myapplication.utilities.Utilities;

public class AddRouteOrHouseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddRouteOrHouseActivity";
    ActivityAddRouteOrHouseBinding binding;

    String[] ulb = {"शहरी स्थानिक संस्था निवडा"};
    String[] ward = {" वार्ड निवडा "};
    String[] locality = {" परिसर "};
    String[] sublocality = {" उपपरिसर "};
    String[] category = {" श्रेणी निवडा "};
    String[] subcategory = {" उप श्रेणी निवडा "};
    Activity activity;
    GpsTracker gpsTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_route_or_house);

        activity=AddRouteOrHouseActivity.this;
        binding.tvMvcoordinates.setOnClickListener(this);
        binding.tvNextDoorAddress.setOnClickListener(this);
        binding.tvNextback.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);

        funSpWard();

    }

    private void funSpWard() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,ulb);
        binding.spUlb.setAdapter(dataAdapter);
        binding.spUlb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> sp1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,ward);
        binding.spWard.setAdapter(sp1);
        binding.spWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> sp2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,locality);
        binding.spLocality.setAdapter(sp2);
        binding.spLocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> sp3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,sublocality);
        binding.spSublocality.setAdapter(sp3);
        binding.spSublocality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         ArrayAdapter<String> sp4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,category);
        binding.spCategory.setAdapter(sp4);
        binding.spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         ArrayAdapter<String> sp5 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,subcategory);
        binding.spSubcategory.setAdapter(sp5);
        binding.spSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                String item = adapterView.getItemAtPosition(pos).toString();
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(pos).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_mvcoordinates:
                binding.llLayout1.setVisibility(View.GONE);
                binding.llLayout2.setVisibility(View.VISIBLE);
                break;
              case R.id.tv_nextDoorAddress:
                binding.llLayout1.setVisibility(View.GONE);
                binding.llLayout3.setVisibility(View.VISIBLE);
                break;
              case R.id.tv_Nextback:
                binding.llLayout2.setVisibility(View.GONE);
                binding.llLayout1.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back:
               // onBackPressed();
                Utilities.launchActivity(this, EngineerHomeActivity.class, true);

                break;


        }
    }
}