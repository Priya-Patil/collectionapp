package com.example.myapplication.wasteCollection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityWastecollectionBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.qrCode.QrCodeWaste;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.tripSummary.adapter.TripSummaryAdapter;
import com.example.myapplication.tripSummary.api.WasteListApi;
import com.example.myapplication.tripSummary.model.TripSummaryModel;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.example.myapplication.wasteCollection.adapter.WasteCollectionAdapter;
import com.example.myapplication.wasteCollection.api.AddWasteDumpApi;
import com.example.myapplication.wasteCollection.model.WasteCollectionModel;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WasteCollectionActivity extends AppCompatActivity  implements View.OnClickListener {
    Activity activity;
    ActivityWastecollectionBinding binding;
    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    WasteCollectionAdapter WastecollectAdapter;
    ArrayList<WasteCollectionModel> modelArrayListWastecollect ;
    String type;
    ProgressDialog progressDialog;
    PrefManager prefManager;
    private ShimmerFrameLayout mShimmerViewContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wastecollection);
        activity = WasteCollectionActivity.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        listeners();

        getWasteList(prefManager.getaid());
    }
    private void listeners() {
        binding.ivBack.setOnClickListener(this);
        binding.tvSelectdate.setOnClickListener(this);
        binding.ivAdd.setOnClickListener(this);
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
    public void onBackPressed() {
        super.onBackPressed();
        Utilities.launchActivity(this, EngineerHomeActivity.class, true);

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

            case R.id.iv_add:
                Utilities.launchActivity(activity, QrCodeWaste.class,false);
                break;
            case R.id.btn_search:
                // onBackPressed();

                Toast.makeText(activity, "coming soon", Toast.LENGTH_SHORT).show();
                break;


        }

    }


    public  void exitmessageDialog(Activity context, String message) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(message);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
                Utilities.launchActivity(activity,EngineerHomeActivity.class,true);

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
    }


    void getWasteList(int att_id)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            WasteListApi wasteListApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(WasteListApi.class);

            wasteListApi.getWasteList(att_id).
                    enqueue(new Callback<ArrayList<WasteCollectionModel>>() {

                        @Override
                        public void onResponse(Call<ArrayList<WasteCollectionModel>> call, Response<ArrayList<WasteCollectionModel>> response) {

                            ArrayList<WasteCollectionModel> bettingResponce = response.body();
                            mShimmerViewContainer.stopShimmer();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            Log.e( "chkonResponse: ", bettingResponce.toString() );
                            if(bettingResponce.size()==0)
                            {
                                Utilities.messageDialog(activity,"No record");
                            }
                            else {
                                setWalletHistory(bettingResponce);
                            }

                        }

                        @Override
                        public void onFailure(Call <ArrayList<WasteCollectionModel>> call, Throwable t) {

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


    void setWalletHistory(ArrayList<WasteCollectionModel> homeResponces)
    {
        binding.rvWasteCollection.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        binding.rvWasteCollection.setItemAnimator(new DefaultItemAnimator());
        binding.rvWasteCollection.setHasFixedSize(true);
        WastecollectAdapter = new WasteCollectionAdapter(activity,homeResponces);

        binding.rvWasteCollection.setAdapter(WastecollectAdapter);
    }


}