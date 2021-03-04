package com.example.myapplication.qrCode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.House.AddHouseActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityQrdumpyardBinding;
import com.example.myapplication.databinding.ActivityTripsummaryBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.engineer.UpdateRouteActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.qrCode.api.QRApi;
import com.example.myapplication.qrCode.model.QRResponce;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.tripSummary.TripSummaryActivity;
import com.example.myapplication.tripSummary.api.WasteListApi;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;

public class QrAddDumpyard extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {
    private IntentIntegrator qrScan;

    Activity activity;
    ActivityQrdumpyardBinding binding;
    ProgressDialog progressDialog;
    PrefManager prefManager;
    int qrid;
    String drywt, wetwt, mixwt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrdumpyard);
        activity = QrAddDumpyard.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
       // requestPermission();

        listners();

        binding.llSearch.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
//        Use this for more customization
//        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
//        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        //  scanBtn.setOnClickListener(this);

        ArrayList<Integer> st = new ArrayList<>();
        int i;
        for ( i=1;i<=100;i++)
        {
            st.add(i);
        }

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,st);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spWetGarbage.setAdapter(aa);
        binding.spDryGarbage.setAdapter(aa);
        binding.spMixGarbage.setAdapter(aa);

        binding.spWetGarbage.setOnItemSelectedListener(this);
        binding.spDryGarbage.setOnItemSelectedListener(this);
        binding.spMixGarbage.setOnItemSelectedListener(this);


    }

    void listners() {
        binding.ivBack.setOnClickListener(this);
        binding.tvSubmitnsend.setOnClickListener(this);
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                binding.llSearch.setVisibility(View.GONE);
                binding.rlTripSummaryMain.setVisibility(View.GONE);

                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Utilities.launchActivity(this, EngineerHomeActivity.class,false);
            }
            else {
               /* binding.llSearch.setVisibility(View.VISIBLE);
               binding.tvScanContent.setText(result.getContents());
               binding.tvScanFormat.setText(result.getFormatName());*/
                Log.e( "onActivityResult: ", result.getContents()+"  "+result.getFormatName());
                isQRcodeavailable(result.getContents(),2);

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        Utilities.launchActivity(this, TripSummaryActivity.class, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                //  onBackPressed();
                Utilities.launchActivity(this, TripSummaryActivity.class, true);
                break;

            case R.id.tv_submitnsend:
                //  onBackPressed();

                addDumpwaste(prefManager.getUserid(),qrid,drywt,wetwt,mixwt);
                break;
        }


       /* llSearch.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
//        Use this for more customization
//        IntentIntegrator integrator = new IntentIntegrator(this);
    integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
     integrator.setPrompt("Scan a barcode");
//        integrator.setCameraId(0);  // Use a specific camera of the device
   integrator.setBeepEnabled(true);
     integrator.setBarcodeImageEnabled(true);
//       integrator.initiateScan();
    }*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.sp_dryGarbage)
        {
            drywt= parent.getSelectedItem().toString();

        }
        else if(parent.getId() == R.id.sp_wetGarbage) {
            wetwt = parent.getSelectedItem().toString();

        }
        else
            {
                mixwt = parent.getSelectedItem().toString();

            }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    void isQRcodeavailable(String id, int type ) {

        if (Utilities.isNetworkAvailable(activity)){
            QRApi qrApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(QRApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            qrApi.isQRcodeavailable( id, type).
                    enqueue(new Callback<ArrayList<QRResponce>>() {
                        @Override
                        public void onResponse(Call<ArrayList<QRResponce>> call,
                                               Response<ArrayList<QRResponce>> response) {

                            ArrayList<QRResponce> qrResponce = response.body();
                            Log.e( "StatusResponce: ", qrResponce.toString());
                            progressDialog.dismiss();
                            if(qrResponce.size()==0){
                                exitmessageDialog(activity,"QR code not available");

                            }
                            else {

                                qrid=qrResponce.get(0).id;
                                Log.e( "idonResponse: ", ""+qrResponce.get(0).id );
                             /*   addHome(null, String.valueOf(latitude), String.valueOf(longitude),
                                        null,null,null,
                                        null,null,prefManager.getUserid(), Integer.parseInt(id),prefManager.getrid());
*/
                            }


                        }
                        @Override
                        public void onFailure(Call<ArrayList<QRResponce>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.e("errorchk", t.getMessage());

                        }
                    });

        } else {
            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public  void exitmessageDialog(Activity context, String message) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(message);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
                Utilities.launchActivity(activity, TripSummaryActivity.class,true);

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
    }

    void addDumpwaste(int  empid, int dumpyardid, String  drywaste,String wetwaste,
                 String mixwaste ) {

        if (Utilities.isNetworkAvailable(activity)){
            WasteListApi wasteListApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(WasteListApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            wasteListApi.addDumpwaste( empid, dumpyardid,  drywaste, wetwaste, mixwaste).
                    enqueue(new Callback<StatusResponce>() {
                        @Override
                        public void onResponse(Call<StatusResponce> call, Response<StatusResponce> response) {

                            StatusResponce loginResponce = response.body();
                            Log.e( "StatusResponce: ", loginResponce.toString());
                            progressDialog.dismiss();

                            exitmessageDialog(activity,"Completed");

                        }
                        @Override
                        public void onFailure(Call<StatusResponce> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.e("errorchk", t.getMessage());

                        }
                    });

        } else {
            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }



    public void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(QrAddDumpyard.this, "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



}