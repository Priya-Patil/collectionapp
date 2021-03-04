package com.example.myapplication.engineer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityEngineerHomeBinding;
import com.example.myapplication.databinding.ActivityRouteSummaryBinding;
import com.example.myapplication.utilities.Utilities;

public class RouteSummaryActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    ActivityRouteSummaryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_route_summary);
        activity = RouteSummaryActivity.this;
        listners();

    }
    void listners() {
        binding.ivBack.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
              //  onBackPressed();
                Utilities.launchActivity(this, EngineerHomeActivity.class, true);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.launchActivity(this, EngineerHomeActivity.class, true);

    }
}
