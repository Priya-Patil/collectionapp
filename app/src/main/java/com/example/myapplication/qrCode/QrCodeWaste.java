package com.example.myapplication.qrCode;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.home.api.HomeApi;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.map.GpsTracker;
import com.example.myapplication.profile.api.ProfileApi;
import com.example.myapplication.qrCode.api.QRApi;
import com.example.myapplication.qrCode.model.QRResponce;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.UriUtils;
import com.example.myapplication.utilities.Utilities;
import com.example.myapplication.wasteCollection.WasteCollectionActivity;
import com.example.myapplication.wasteCollection.api.AddWasteDumpApi;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrCodeWaste extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "activity";
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


    ImageView img_before,img_after;

    int var=0;
    TextView txt_id;
    int qrid=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        activity= QrCodeWaste.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);

        scanBtn = (Button) findViewById(R.id.scan_button);
        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
         //requestPermission();
        chkLocationManager();

        gpsTracker = new GpsTracker(activity);
         latitude = gpsTracker.getLatitude();
         longitude = gpsTracker.getLongitude();
        Log.e( "getLocation: ",""+latitude+" "+longitude+" ");

        llSearch.setVisibility(View.GONE);
       dialog_add_wast_type();
     /*   IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode or QRcode");
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
   */ }

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
                //AddWasteDump(prefManager.getaid(),1,null);
                isQRcodeavailable(result.getContents(),1);

            }
        }

         else {
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



    void AddWasteDump(int  att_id, int home_id, String  comment ) {

        if (Utilities.isNetworkAvailable(activity)){
            AddWasteDumpApi addWasteDumpApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(AddWasteDumpApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            addWasteDumpApi.AddWasteDump( att_id, home_id,  comment).
                    enqueue(new Callback<StatusResponce>() {
                        @Override
                        public void onResponse(Call<StatusResponce> call, Response<StatusResponce> response) {

                            StatusResponce loginResponce = response.body();
                            Log.e( "StatusResponce: ", loginResponce.toString());
                            progressDialog.dismiss();

                            Utilities.launchActivity(activity,WasteCollectionActivity.class,true);
                          //  dialog_add_wast_type();
                            //dialouge();

                          //exitmessageDialog(activity,"Collection is done");



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

    public  void exitmessageDialog(Activity context, String message) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        mBuilder.setTitle(message);
        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
                Utilities.launchActivity(activity, WasteCollectionActivity.class,true);

            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
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

                                //imp
                                qrid=qrResponce.get(0).id;
                                txt_id.setText("House no: "+qrResponce.get(0).barcode);
                               // AddWasteDump(prefManager.getaid(), qrResponce.get(0).id,null);

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




    public void dialog_add_wast_type( ) {

        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.custom_dialog_add_wast_type, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView btnConfirm,btn_cancel,btn_scanQr;
        RadioButton radioMixGarbage,radioLockHome,radioDiffGarbage,radioNotGiven;
        RadioGroup radioGroup;

        btn_scanQr=view.findViewById(R.id.btn_scanQr);

        radioMixGarbage=view.findViewById(R.id.radioMixGarbage);
        radioLockHome=view.findViewById(R.id.radioLockHome);
        radioDiffGarbage=view.findViewById(R.id.radioDiffGarbage);
        radioNotGiven=view.findViewById(R.id.radioNotGiven);
        txt_id=view.findViewById(R.id.txt_id);

        btn_cancel=view.findViewById(R.id.btn_cancel);
        btnConfirm=view.findViewById(R.id.btn_confirm);

        radioMixGarbage.isActivated();
        //  radioButton=view.findViewById(R.id.radioButton);
        radioGroup=view.findViewById(R.id.radioGroup);

        btn_scanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(QrCodeWaste.this);
                integrator.setPrompt("Scan QRcode");
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);

               // Toast.makeText(activity,"scan Qr ",Toast.LENGTH_SHORT).show();
            }
        });

        radioMixGarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var=1;
              //  Toast.makeText(activity,""+var,Toast.LENGTH_SHORT).show();
            }
        });

        radioLockHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var=2;
              //  Toast.makeText(activity,""+var,Toast.LENGTH_SHORT).show();
            }
        });

        radioDiffGarbage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var=3;
              //  Toast.makeText(activity,""+var,Toast.LENGTH_SHORT).show();
            }
        });

        radioNotGiven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var=4;
               // Toast.makeText(activity,""+var,Toast.LENGTH_SHORT).show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                onBackPressed();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(qrid==0 )
                {
                   // Toast.makeText(activity, "Please scan QR code", Toast.LENGTH_SHORT).show();
                   Utilities.messageDialog(QrCodeWaste.this,"Please scan QR code");
                }
                else {
                    alertDialog.dismiss();
                    AddWasteDump(prefManager.getaid(), qrid, String.valueOf(var));
                 //   Toast.makeText(activity, "mmm" + var, Toast.LENGTH_SHORT).show();
                }


            }
        });
        alertDialog.show();
        alertDialog.setCancelable(false);
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
                        Toast.makeText(QrCodeWaste.this, "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


