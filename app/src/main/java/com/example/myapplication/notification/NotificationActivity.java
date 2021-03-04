package com.example.myapplication.notification;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityNotificationBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.notification.adapter.NotificationAdapter;
import com.example.myapplication.notification.model.NotificationModel;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity  implements View.OnClickListener {

    Activity activity;
    ActivityNotificationBinding binding;

    private String[] timeList = new String[]{"10 Am","2 pm","3 pm"};
    private String[] dateList = new String[]{"10-1-2021","20-2-2021","25-2-2021"};
    private String[] titleList = new String[]{"abcd","pqrs","wxyz"};

    private String[] addressList = new String[]{"cidco","aurangabad","jalana"};

    private String[] descList = new String[]{"ytwrdt9weifueyt;qigfyegf98eoifheyge89heiygiuoe","aytdiausdfuysisudgsdg","ytsdeygfisyfo8etfyuguhoew"};

    NotificationAdapter videosAdapter;
    ArrayList<NotificationModel> modelArrayListVideos ;
    String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        activity = NotificationActivity.this;
        listeners();
        setMedicHisrory();
    }

    private void listeners() {
        binding.ivBack.setOnClickListener(this);
    }

    private void setMedicHisrory() {
        modelArrayListVideos = arrayVideos();
        binding.rvMedicHistory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvMedicHistory.setItemAnimator(new DefaultItemAnimator());
        binding.rvMedicHistory.setHasFixedSize(true);
        videosAdapter = new NotificationAdapter(this, modelArrayListVideos,
                new NotificationAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
        binding.rvMedicHistory.setAdapter(videosAdapter);
    }

    private ArrayList<NotificationModel> arrayVideos(){
        ArrayList<NotificationModel> list = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            NotificationModel videosModel = new NotificationModel();
            videosModel.setDate(dateList[i]);//by sneha
            videosModel.setDesc(descList[i]);//by sneha
            videosModel.setTime(timeList[i]);//by sneha
            videosModel.setTitile(titleList[i]);//by sneha
            videosModel.setAddtess(addressList[i]);//by sneha

            // videosModel.setImage_drawable(videosImgList[i]);
            list.add(videosModel);
        }
        return list;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_back:
             //   onBackPressed();
                Utilities.launchActivity(this, EngineerHomeActivity.class, true);

                break;

        }

    }
}