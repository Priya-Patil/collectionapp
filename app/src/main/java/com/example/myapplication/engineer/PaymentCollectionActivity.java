package com.example.myapplication.engineer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityPaymentCollectionBinding;
import com.example.myapplication.databinding.ActivityRouteSummaryBinding;
import com.example.myapplication.utilities.Utilities;

public class PaymentCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;
    ActivityPaymentCollectionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_collection);
        activity = PaymentCollectionActivity.this;
        listners();
    }

    private void listners() {
        binding.imgOpenDrawer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_openDrawer:
                //onBackPressed();
                Utilities.launchActivity(this, EngineerHomeActivity.class, true);
                break;

        }
    }
}