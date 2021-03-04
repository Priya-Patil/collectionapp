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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.complaint.ComplaintActivity;
import com.example.myapplication.complaint.api.ComplaintApi;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.home.model.StatusResponce;
import com.example.myapplication.map.GpsTracker;
import com.example.myapplication.qrCode.api.QRApi;
import com.example.myapplication.qrCode.model.QRResponce;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.UriUtils;
import com.example.myapplication.utilities.Utilities;
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

public class QrAddComplaint extends AppCompatActivity implements View.OnClickListener {


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


    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_CAMERA = 2;

    private int PICK_IMAGE_REQUEST2 = 3;
    private int REQUEST_CAMERA2 = 4;

    File path,path2;
    String fullpath;
    String fullpath2;
    Uri uri, uri2;
    ArrayList<String> a;

    ImageView img_before,img_after;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        activity= QrAddComplaint.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);

        scanBtn = (Button) findViewById(R.id.scan_button);
        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        a = new ArrayList<>();
        //requestPermission();
        chkLocationManager();

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
            if (requestCode == PICK_IMAGE_REQUEST) {

                if (data.getData() != null) {
                    uri = data.getData();
                    Log.e(TAG, "uri " + uri);

                    //fullpath= getPath(uri);
                    fullpath = UriUtils.getPathFromUri(this, uri);

                    Log.e(TAG, "fullpath " + fullpath);
                    if (fullpath.endsWith("jpg") || fullpath.endsWith("jpeg") || fullpath.endsWith("png")) {
                        a.add(fullpath);
                    } else {
                        // Utilities.dialogMessage(activity,"Select only images");
                    }

                }

                img_before.setImageURI(uri);

            }


            if (requestCode == PICK_IMAGE_REQUEST2) {

                if (data.getData() != null) {
                    uri2 = data.getData();
                    Log.e(TAG, "uri " + uri2);

                    //fullpath= getPath(uri);
                    fullpath2 = UriUtils.getPathFromUri(this, uri2);

                    Log.e(TAG, "fullpath " + fullpath);
                    if (fullpath2.endsWith("jpg") || fullpath2.endsWith("jpeg") || fullpath2.endsWith("png")) {
                        a.add(fullpath2);
                    } else {
                        // Utilities.dialogMessage(activity,"Select only images");
                    }

                }

                img_after.setImageURI(uri2);

            }

            else if (requestCode == REQUEST_CAMERA) {

                Bitmap photo = null;

                if(!((Bitmap) data.getExtras().get("data") == null)) {
                    photo = (Bitmap) data.getExtras().get("data");
                }else
                {

                }
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                // Uri tempUri = getImageUri(getApplicationContext(), photo);
                uri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(uri));

                //System.out.println(tempUri);
                Log.d(TAG, "pthth " + uri + " final " + finalFile);

                fullpath = String.valueOf(finalFile);
                img_before.setImageURI(uri);

                Log.d(TAG, "fullpath sss  " + fullpath);
            }

            else if (requestCode == REQUEST_CAMERA2) {

                Bitmap photo = null;

                if(!((Bitmap) data.getExtras().get("data") == null)) {
                    photo = (Bitmap) data.getExtras().get("data");
                }else
                {

                }
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                // Uri tempUri = getImageUri(getApplicationContext(), photo);
                uri2 = getImageUri2(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(uri2));

                //System.out.println(tempUri);
                Log.d(TAG, "pthth " + uri + " final " + finalFile);

                fullpath2 = String.valueOf(finalFile);
                img_after.setImageURI(uri2);

                Log.e(TAG, "fullpathsss  " + fullpath2);
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
            ComplaintApi complaintApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(ComplaintApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            complaintApi.addComplaint( att_id, home_id,  comment).
                    enqueue(new Callback<StatusResponce>() {
                        @Override
                        public void onResponse(Call<StatusResponce> call, Response<StatusResponce> response) {

                            StatusResponce loginResponce = response.body();
                            Log.e( "StatusResponce: ", loginResponce.toString());
                            progressDialog.dismiss();

                            dialouge();

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
                Utilities.launchActivity(activity, ComplaintActivity.class,true);

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

                                AddWasteDump(prefManager.getaid(), qrResponce.get(0).id,null);

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


    private void dialouge() {
        {

            // AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyDialogTheme));
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.cutom_dialog_captureimage, null);
            dialogBuilder.setView(dialogView);

            //   dialogBuilder.setTitle("Capture Image");

            TextView tv_Ok,tv_cancle;
            ImageView img_bin;
            RelativeLayout img1,img2;
            img_bin= dialogView.findViewById(R.id.img_bin);
            img1= dialogView.findViewById(R.id.img1);
            img2= dialogView.findViewById(R.id.img2);
            img_before= dialogView.findViewById(R.id.img_before);
            img_after= dialogView.findViewById(R.id.img_after);
            img_bin.setBackgroundColor(getResources().getColor(R.color.transparent));


            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            alertDialog.show();

            //dialogView.setBackgroundColor(getResources().getColor(R.color.white));
            // alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.circular_tvborder);
            //   alertDialog.getWindow().setStatusBarColor( getResources().getColor(R.color.black));

            tv_cancle= dialogView.findViewById(R.id.tv_cancle);
            tv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    Utilities.launchActivity(activity, ComplaintActivity.class,true);

                }
            });

            tv_Ok= dialogView.findViewById(R.id.tv_Ok);
            tv_Ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                 //   Toast.makeText(getApplicationContext(),"ok was clicked",Toast.LENGTH_SHORT).show();
                     alertDialog.dismiss();

                     addWasteCollectionimg(fullpath,fullpath2,prefManager.getUserid());
                   // Utilities.launchActivity(activity, ComplaintActivity.class,true);
                }
            });

            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   ShowImageChooser();
                }
            });

            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ShowImageChooser2();
                }
            });
        }
    }

    private void ShowImageChooser() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void ShowImageChooser2() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA2);

                } else if (items[item].equals("Choose from Library")) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST2);

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public Uri getImageUri2(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path2 = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path2);
    }


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;

    }


    private void addWasteCollectionimg(String imgpath, String imgpath2, int userid) {

        if (Utilities.isNetworkAvailable(activity)) {
            Log.e("addWasteCollectionimg: ", imgpath + imgpath2 + userid);
            progressDialog.setMessage("Loading...");
            progressDialog.setTitle("Update in Process");
            progressDialog.show();
            progressDialog.setCancelable(false);

            if (imgpath == null && imgpath2!=null) {

                path2 = new File(imgpath2);
                Log.e(TAG, "imgggg " + path2 + " " + userid + " uri " + uri);
                RequestBody requestFile2 =
                        RequestBody.create(
                                okhttp3.MediaType.parse(getContentResolver().getType(uri2)),
                                path2
                        );

                MultipartBody.Part body2 =
                        MultipartBody.Part.createFormData("afterimg", path2.getName(), requestFile2);

                RequestBody id = RequestBody.create(okhttp3.MediaType.parse("text/plain"),
                        String.valueOf(userid));

                AddWasteDumpApi addWasteDumpApi = RetrofitClientInstance.getRetrofitInstanceServer().
                        create(AddWasteDumpApi.class);

                addWasteDumpApi.addWasteCollectionimg(null, body2, id).
                        enqueue(new Callback<StatusResponce>() {
                            @Override
                            public void onResponse(Call<StatusResponce> call,
                                                   Response<StatusResponce> response) {
                                StatusResponce profileImageUpdateResponse = response.body();

                                Log.e("imgonResponse: ", profileImageUpdateResponse.toString());

                                progressDialog.dismiss();
                                exitmessageDialog(activity, "Images added successfully");

                            }

                            @Override
                            public void onFailure(Call<StatusResponce> call, Throwable t) {
                                Log.e(TAG, "ProfileImageUpdateResponse " + t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
            }


            if (imgpath != null && imgpath2==null) {

                path = new File(imgpath);
                Log.e(TAG, "imgggg " + path + " " + userid + " uri " + uri);
                RequestBody requestFile =
                        RequestBody.create(
                                okhttp3.MediaType.parse(getContentResolver().getType(uri)),
                                path
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("beforeimg", path.getName(), requestFile);

                RequestBody id = RequestBody.create(okhttp3.MediaType.parse("text/plain"),
                        String.valueOf(userid));

                AddWasteDumpApi addWasteDumpApi = RetrofitClientInstance.getRetrofitInstanceServer().
                        create(AddWasteDumpApi.class);

                addWasteDumpApi.addWasteCollectionimg(body, null, id).
                        enqueue(new Callback<StatusResponce>() {
                            @Override
                            public void onResponse(Call<StatusResponce> call,
                                                   Response<StatusResponce> response) {
                                StatusResponce profileImageUpdateResponse = response.body();

                                Log.e("imgonResponse: ", profileImageUpdateResponse.toString());

                                progressDialog.dismiss();
                                exitmessageDialog(activity, "Images added successfully");

                            }

                            @Override
                            public void onFailure(Call<StatusResponce> call, Throwable t) {
                                Log.e(TAG, "ProfileImageUpdateResponse " + t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
            }  if (imgpath != null && imgpath2 != null) {

                path = new File(imgpath);
                Log.e(TAG, "imgggg " + path + " " + userid + " uri " + uri);
                RequestBody requestFile =
                        RequestBody.create(
                                okhttp3.MediaType.parse(getContentResolver().getType(uri)),
                                path
                        );

                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("beforeimg", path.getName(), requestFile);

                path2 = new File(imgpath2);
                Log.e(TAG, "imgggg " + path2 + " " + userid + " uri " + uri);
                RequestBody requestFile2 =
                        RequestBody.create(
                                okhttp3.MediaType.parse(getContentResolver().getType(uri2)),
                                path2
                        );

                MultipartBody.Part body2 =
                        MultipartBody.Part.createFormData("afterimg", path2.getName(), requestFile2);

                RequestBody id = RequestBody.create(okhttp3.MediaType.parse("text/plain"),
                        String.valueOf(userid));

                AddWasteDumpApi addWasteDumpApi = RetrofitClientInstance.getRetrofitInstanceServer().
                        create(AddWasteDumpApi.class);

                addWasteDumpApi.addWasteCollectionimg(body, body2, id).
                        enqueue(new Callback<StatusResponce>() {
                            @Override
                            public void onResponse(Call<StatusResponce> call,
                                                   Response<StatusResponce> response) {
                                StatusResponce profileImageUpdateResponse = response.body();

                                Log.e("imgonResponse: ", profileImageUpdateResponse.toString());

                                progressDialog.dismiss();
                                exitmessageDialog(activity, "Images added successfully");

                            }

                            @Override
                            public void onFailure(Call<StatusResponce> call, Throwable t) {
                                Log.e(TAG, "ProfileImageUpdateResponse " + t.getMessage());
                                progressDialog.dismiss();
                            }
                        });
            }   if (imgpath == null && imgpath2 == null) {

                progressDialog.dismiss();

                Utilities.launchActivity(activity, ComplaintActivity.class,true);
            }
        } else {


            //Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            Utilities.messageDialog(activity, getResources().getString(R.string.check_internet));

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
                        Toast.makeText(QrAddComplaint.this, "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


}


