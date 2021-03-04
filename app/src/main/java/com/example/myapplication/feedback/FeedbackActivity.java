package com.example.myapplication.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityFeedbackBinding;
import com.example.myapplication.feedback.adapter.FeedbackAdapter;
import com.example.myapplication.feedback.model.FeedbackModel;
import com.example.myapplication.homeMenu.MainActivity;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {


    Activity activity;
    ActivityFeedbackBinding binding;

    private int[] feedbackList = new int[]{R.string.notREgular,R.string.notOnTime,R.string.notScanning,R.string.wrongSelection,R.string.notProffesional,R.string.notpacedBins,R.string.litteringWalkway};

    FeedbackAdapter videosAdapter;
    ArrayList<FeedbackModel> modelArrayListFeedback ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        activity = FeedbackActivity.this;
        btnListerner();
        setFeedback();
    }

    void btnListerner() {

        binding.ivBack.setOnClickListener(this);
    }
    private void setFeedback() {
        modelArrayListFeedback = arrayVideos();
        binding.rvFeedback.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvFeedback.setItemAnimator(new DefaultItemAnimator());
        binding.rvFeedback.setHasFixedSize(true);
        videosAdapter = new FeedbackAdapter(this, modelArrayListFeedback,
                new FeedbackAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                });
        binding.rvFeedback.setAdapter(videosAdapter);
    }
    private ArrayList<FeedbackModel> arrayVideos(){
        ArrayList<FeedbackModel> list = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            FeedbackModel feedbackModel = new FeedbackModel();
            feedbackModel.setAmount(feedbackList[i]);//by sneha
            list.add(feedbackModel);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.iv_back:
                Utilities.launchActivity(activity, MainActivity.class, true);
                break;

        }
    }
}
