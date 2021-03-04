package com.example.myapplication.engineer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityUpdateRouteBinding;
import com.example.myapplication.engineer.adapter.WasteTypeAdapter;
import com.example.myapplication.engineer.model.WasteTypeModel;
import com.example.myapplication.notification.adapter.NotificationAdapter;
import com.example.myapplication.notification.model.NotificationModel;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

public class UpdateRouteActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityUpdateRouteBinding binding;
    private String[] timeList = new String[]{"10 Am","2 pm","3 pm"};
    private Integer[] dateList = new Integer[]{R.drawable.ic_baseline_delete_24,R.drawable.ic_person,R.drawable.ic_person,R.drawable.ic_baseline_delete_forever_24};
    private String[] titleList = new String[]{"abcd","pqrs","wxyz"};
    private String[] descList = new String[]{"ytwrdt9weifueyt;qigfyegf98eoifheyge89heiygiuoe","aytdiausdfuysisudgsdg","ytsdeygfisyfo8etfyuguhoew"};

    WasteTypeAdapter wasteTypeAdapter;
    ArrayList<WasteTypeModel> modelArrayListVideos ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_update_route);
        listners();
        setMedicHisrory();

    }
    void listners() {
        binding.ivBack.setOnClickListener(this);
    }
    private void setMedicHisrory() {
        modelArrayListVideos = arrayVideos();
        binding.rvMedicHistory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvMedicHistory.setItemAnimator(new DefaultItemAnimator());
        binding.rvMedicHistory.setHasFixedSize(true);
        wasteTypeAdapter = new WasteTypeAdapter(this, modelArrayListVideos,
                new WasteTypeAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
        binding.rvMedicHistory.setAdapter(wasteTypeAdapter);
    }

    private ArrayList<WasteTypeModel> arrayVideos(){
        ArrayList<WasteTypeModel> list = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            WasteTypeModel wasteTypeModel = new WasteTypeModel();
            wasteTypeModel.setImage(dateList[i]);//by sneha

            list.add(wasteTypeModel);
        }
        return list;
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
}