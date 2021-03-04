package com.example.myapplication.qrCode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.House.AddHouseActivity;
import com.example.myapplication.R;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.engineer.UpdateRouteActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.login.api.LoginApi;
import com.example.myapplication.login.model.LoginResponce;
import com.example.myapplication.map.GpsTracker;
import com.example.myapplication.qrCode.api.QRApi;
import com.example.myapplication.qrCode.model.QRResponce;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class QrCodeAddHome extends AppCompatActivity implements View.OnClickListener {


    private Button scanBtn;
    private TextView tvScanFormat, tvScanContent;
    private LinearLayout llSearch;
    //qr code scanner object
    private IntentIntegrator qrScan;

    ProgressDialog progressDialog;

    Activity activity;
    GpsTracker gpsTracker;
    double latitude,longitude;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        activity=QrCodeAddHome.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
       // requestPermission();

        scanBtn = (Button) findViewById(R.id.scan_button);
        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);

        chkLocationManager();

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
            gpsTracker = new GpsTracker(activity);
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e( "getLocation: ",""+latitude+" "+longitude+" ");

            llSearch.setVisibility(View.GONE);
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("Scan a barcode or QRcode");
            integrator.setOrientationLocked(true);
            integrator.initiateScan();
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
            integrator.setPrompt("Scan a barcode");
            integrator.setBeepEnabled(true);
            integrator.setBarcodeImageEnabled(true);

        }


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                llSearch.setVisibility(View.GONE);
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                Utilities.launchActivity(this, EngineerHomeActivity.class,false);
            } else {


                llSearch.setVisibility(View.VISIBLE);
                tvScanContent.setText(result.getContents());
                tvScanFormat.setText(result.getFormatName());
                Log.e( "onActivityResult: ", result.getContents()+"  "+result.getFormatName());
                isQRcodeavailable(result.getContents(),1);

               /* addHome(1, String.valueOf(latitude), String.valueOf(longitude),
                        null,null,null,
                        null,null);*/
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View v) {
    }




    void chkLocationManager()
    {
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
            new AlertDialog.Builder(activity)
                    .setMessage("GPS network not enabled")
                    .setCancelable(false)
                    .setPositiveButton("Open location settings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.onBackPressed();
                            //Toast.makeText(activity, "nooo", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }
        //enabled
        else {
            //    Toast.makeText(LocationTraceActivity.this, "enabled", Toast.LENGTH_SHORT).show();

        }
    }
    void addHome(String  ward_id, String latitude, String  longitude,String name,
                    String mobile, String address, String family_members, String waste_id, int empid, int qrid,int routeid ) {

        if (Utilities.isNetworkAvailable(activity)){
            HomeApi homeApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(HomeApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            homeApi.AddHouse( ward_id, latitude,  longitude, name,
                     mobile,  address,  family_members,  waste_id,empid, qrid,routeid).
                    enqueue(new Callback<StatusResponce>() {
                        @Override
                        public void onResponse(Call<StatusResponce> call, Response<StatusResponce> response) {

                            StatusResponce loginResponce = response.body();
                            Log.e( "StatusResponce: ", loginResponce.toString());
                            progressDialog.dismiss();

                            exitmessageDialog(activity,"geo tagging is done");

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

    void isQRcodeavailable(String  id, int type ) {

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

                          addHome(null, String.valueOf(latitude), String.valueOf(longitude),
                        null,null,null,
                        null,null,prefManager.getUserid(),
                                 qrResponce.get(0).id,prefManager.getrid());

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
                Utilities.launchActivity(activity, AddHouseActivity.class,true);

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
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
                        Toast.makeText(QrCodeAddHome.this, "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }




}