package com.example.myapplication.engineer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.House.AddHouseActivity;
import com.example.myapplication.R;
import com.example.myapplication.attendance.api.AttendanceApi;
import com.example.myapplication.attendance.model.AttendanceResponce;
import com.example.myapplication.databinding.ActivityEngineerHomeBinding;
import com.example.myapplication.engineer.adapter.HomeEngiAdapter;
import com.example.myapplication.engineer.model.HomeEngiModel;
import com.example.myapplication.homeMenu.adapter.HomeMenuAdapter;
import com.example.myapplication.homeMenu.model.HomeMenuModel;
import com.example.myapplication.login.api.LoginApi;
import com.example.myapplication.login.model.LoginResponce;
import com.example.myapplication.map.GpsTracker;
import com.example.myapplication.notification.NotificationActivity;
import com.example.myapplication.qrCode.QrAddComplaint;
import com.example.myapplication.qrCode.QrCodeWaste;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.route.api.RouteApi;
import com.example.myapplication.route.model.RouteResponce;
import com.example.myapplication.tripSummary.TripSummaryActivity;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.Utilities;
import com.example.myapplication.vehicle.api.VehicleApi;
import com.example.myapplication.vehicle.model.VehicleResponce;
import com.example.myapplication.wasteCollection.WasteCollectionActivity;
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
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EngineerHomeActivity extends AppCompatActivity implements
        View.OnClickListener, AdapterView.OnItemSelectedListener, OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Activity activity;
    ActivityEngineerHomeBinding binding;
    private static final String TAG = "EngineerHomeActivity";
    HomeEngiAdapter adapter;
    ArrayList<HomeEngiModel> modelArrayListMenu ;
    String[]  spRoute = {"मार्ग निवडा","हर्सूल रोड", "आकाशवाणी", "जालना रोड", "जळगाव रोड", "सिडको "};
    String[]  spVehicale = {"वाहन निवडा","गाडी क्रमांक 1 ", "गाडी क्रमांक 2", "गाडी क्रमांक 3", "गाडी क्रमांक 4", "गाडी क्रमांक 5"};

    private int[] homeMenuImage = new int[]{R.drawable.ic_recyclebin, R.drawable.ic_truck,
            R.drawable.ic_addhome,R.drawable.ic_location};
/*
 private int[] homeMenuImage = new int[]{R.drawable.ic_recyclebin, R.drawable.ic_truck,
            R.drawable.ic_notification,R.drawable.ic_payment, R.drawable.ic_addhome,R.drawable.ic_location};
*/

   // private int[] homeMenuText = new int[]{R.string.wastecollection1,R.string.tripsummary1,R.string.complaintnotification1,R.string.paymentcollection1,R.string.addhousegate1,R.string.viewrout1};

    private int[] homeMenuText = new int[]{R.string.wastecollection1,
            R.string.tripsummary1,
            R.string.addhousegate1,R.string.viewrout1};

    ProgressDialog progressDialog;
    PrefManager prefManager;
    int rid, vid;

    GpsTracker gpsTracker;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_engineer_home);
        activity = EngineerHomeActivity.this;
        progressDialog=new ProgressDialog(activity);
        prefManager=new PrefManager(activity);
        circleImageView=findViewById(R.id.overlapImage);

        listners();
        requestPermission();
        chkLocationManager();

        gpsTracker = new GpsTracker(activity);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        Log.e( "getLocation: ",""+latitude+" "+longitude+" ");

        if(prefManager.getAttendance()==false)
        {
            binding.tvEnggIdText.setText(getResources().getString(R.string.enggId)+prefManager.getUserid());
            binding.tvOndutyText.setText(""+prefManager.getvname());
            dialouge();
        }
      //  dialouge();
        binding.tvEnggIdText.setText(getResources().getString(R.string.enggId)+prefManager.getUserid());
        binding.tvOndutyText.setText(""+prefManager.getvname());
       // String imageUri = "http://nagarparishad.avistore.in/images/1590265550.png";

        Picasso.with(getApplicationContext()).load(RetrofitClientInstance.IMG_URL+prefManager.getprofileimg()).into(circleImageView);

        // circleImageView.setImageURI(Uri.parse(RetrofitClientInstance.IMG_URL+prefManager.getprofileimg()));
       // circleImageView.setImageURI(Uri.parse("http://nagarparishad.avistore.in/public/images/1590265550.png"));

        setHomeMenu();

    }

    void listners()
    {
        binding.imgOpenDrawer.setOnClickListener(this);
        binding.includes.rlViewroute.setOnClickListener(this);
        binding.includes.rlTripSummary.setOnClickListener(this);
        binding.includes.rlWastecollection.setOnClickListener(this);
      //  binding.includes.rlWastCollOndemd.setOnClickListener(this);
        binding.includes.rlComplaintNoti.setOnClickListener(this);
        binding.includes.rlPayCollection.setOnClickListener(this);
        binding.includes.rlAddhousegate.setOnClickListener(this);

    }

    public void onBackPressed() {
        finish();
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.img_openDrawer:
                binding.drlOpener.openDrawer(Gravity.LEFT); //Edit Gravity.START need API 14
                break;
            case R.id.rl_viewroute:
                Utilities.launchActivity(activity,UpdateRouteActivity.class,false);
                break;
            case R.id.rl_tripSummary:
                Utilities.launchActivity(activity, TripSummaryActivity.class,false);
                break;
            case R.id.rl_wastecollection:
                binding.drlOpener.closeDrawer(Gravity.LEFT);
                Utilities.launchActivity(activity, QrCodeWaste.class,false);
                break;
        /*    case R.id.rl_WastCollOndemd:
                binding.drlOpener.closeDrawer(Gravity.LEFT);
                Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show();
                break;
*/
            case R.id.rl_complaintNoti:
                binding.drlOpener.closeDrawer(Gravity.LEFT);
                Utilities.launchActivity(activity, NotificationActivity.class,false);
                break;

            case R.id.rl_payCollection:
                binding.drlOpener.closeDrawer(Gravity.LEFT);
                Utilities.launchActivity(activity,PaymentCollectionActivity.class,false);
                break;

            case R.id.rl_addhousegate:
                binding.drlOpener.closeDrawer(Gravity.LEFT);
                Utilities.launchActivity(activity, AddHouseActivity.class,false);
                break;

        }
    }
    void setHomeMenu()
    {
        modelArrayListMenu = arrayMenu();

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        binding.rvMenu1.setItemAnimator(new DefaultItemAnimator());
        binding.rvMenu1.setHasFixedSize(true);
        binding.rvMenu1.setLayoutManager(layoutManager);
        adapter = new HomeEngiAdapter(this, modelArrayListMenu,
                new HomeEngiAdapter.ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                    }
                });
        binding.rvMenu1.setAdapter(adapter);
        // progressDialog.dismiss();
    }

    private ArrayList<HomeEngiModel> arrayMenu(){
        ArrayList<HomeEngiModel> list = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            HomeEngiModel homeMenuModel = new HomeEngiModel();
            homeMenuModel.setName(homeMenuText[i]);
            homeMenuModel.setImage_drawable(homeMenuImage[i]);
            list.add(homeMenuModel);
        }
        return list;
    }

    void dialouge()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.cutom_dialog, null);
        dialogBuilder.setView(dialogView);
       // SwitchCompat switchButton1=dialogView.findViewById(R.id.switchButton);
        TextView tv_Ok;
        Spinner sp_route =  dialogView.findViewById(R.id.sp_route);
        Spinner sp_vehicle =  dialogView.findViewById(R.id.sp_vehicle);
        TextView tv_enggId =  dialogView.findViewById(R.id.tv_enggId);
        TextView tv_enggName =  dialogView.findViewById(R.id.tv_enggName);

        tv_enggName.setText(prefManager.getName());
        tv_enggId.setText("ENG: "+prefManager.getUserid());
        getRoute(sp_route);
        getVehicle(sp_vehicle);
         AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);

        tv_Ok= dialogView.findViewById(R.id.tv_Ok);

        tv_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefManager.setAttendance(true);
                if(sp_route.getSelectedItem().toString().equals("Select Route") || sp_vehicle.getSelectedItem().toString().equals("Select Vehicle")
                 )
                {
                    Toast.makeText(activity, "Select all details", Toast.LENGTH_SHORT).show();
                   // Utilities.messageDialog(activity,"Select all details");
                }
                else {
                    alertDialog.dismiss();
                    takeAttendance(prefManager.getUserid(),rid,vid);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),spRoute[position] , Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(),spVehicale[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void getRoute(Spinner spinAmt)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            RouteApi routeApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(RouteApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            routeApi.getRoute().
                    enqueue(new Callback<ArrayList<RouteResponce>>() {

                        @Override
                        public void onResponse(Call<ArrayList<RouteResponce>> call, Response<ArrayList<RouteResponce>> response) {

                            ArrayList<RouteResponce> walletResponces = response.body();
                            Log.e(TAG, "getWalletType: "+walletResponces.toString());


                            Log.e(TAG, "onResponse: "+walletResponces.toString() );

                            String[] spinnerArray = new String[walletResponces.size()];
                            HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
                            for (int i = 0; i < walletResponces.size(); i++)
                            {
                                spinnerMap.put(i, String.valueOf(walletResponces.get(i).id));
                                spinnerArray[i] = walletResponces.get(i).title;
                            }
                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinAmt.setAdapter(adapter);
                            //spinAmt.setSelection(0,true);;


                            spinAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                                    String name = spinAmt.getSelectedItem().toString();
                                    String id1 = spinnerMap.get(spinAmt.getSelectedItemPosition());
                                    rid= spinAmt.getSelectedItemPosition();
                                    prefManager.setrid(rid);
                                    Toast.makeText(activity,"rid"+id1 +" "+name,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                            progressDialog.dismiss();
                            //Utilities.launchActivity(activity, HomeButtonsActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call <ArrayList<RouteResponce>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }

    void getVehicle(Spinner spinAmt)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            VehicleApi vehicleApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(VehicleApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            vehicleApi.getVehicles().
                    enqueue(new Callback<ArrayList<VehicleResponce>>() {

                        @Override
                        public void onResponse(Call<ArrayList<VehicleResponce>> call, Response<ArrayList<VehicleResponce>> response) {

                            ArrayList<VehicleResponce> walletResponces = response.body();
                            Log.e(TAG, "getWalletType: "+walletResponces.toString());
                            Log.e(TAG, "onResponse: "+walletResponces.toString() );
                            String[] spinnerArray = new String[walletResponces.size()];
                            HashMap<Integer,String> spinnerMap = new HashMap<Integer, String>();
                            for (int i = 0; i < walletResponces.size(); i++)
                            {
                                spinnerMap.put(i, String.valueOf(walletResponces.get(i).id));
                                spinnerArray[i] = walletResponces.get(i).vehicle_type;
                            }
                            ArrayAdapter<String> adapter =new ArrayAdapter<String>(activity,
                                    android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinAmt.setAdapter(adapter);
                            //spinAmt.setSelection(0,true);;


                            spinAmt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

                                    String name = spinAmt.getSelectedItem().toString();
                                    String id1 = spinnerMap.get(spinAmt.getSelectedItemPosition());
                                    vid= spinAmt.getSelectedItemPosition();
                                    prefManager.setvname(spinAmt.getSelectedItem().toString());
                                    Toast.makeText(activity,"vid"+id1 +" "+name,Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                            progressDialog.dismiss();
                            //Utilities.launchActivity(activity, HomeButtonsActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call <ArrayList<VehicleResponce>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();

        }
    }


    void takeAttendance(int empid, int routeid, int vno) {

        if (Utilities.isNetworkAvailable(activity)){
            AttendanceApi attendanceApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(AttendanceApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            attendanceApi.AddAttendance(empid, routeid, vno).
                    enqueue(new Callback<AttendanceResponce>() {

                        @Override
                        public void onResponse(Call<AttendanceResponce> call, Response<AttendanceResponce> response) {
                            AttendanceResponce loginResponce = response.body();
                            Log.e( "takeAttendance: ", loginResponce.toString());
                            prefManager.setaid(loginResponce.id);
                            progressDialog.dismiss();
                        }
                        @Override
                        public void onFailure(Call<AttendanceResponce> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.e("errorchk", t.getMessage());

                        }
                    });

        } else {
            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)//ACCESS_FINE_LOCATION
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//PRIORITY_BALANCED_POWER_ACCURACY
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        double lat=latLng.latitude;
        double lag=latLng.longitude;

        Log.e( "onLocationChanged: ", ""+lat+lag );
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(50));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
                        Toast.makeText(EngineerHomeActivity.this, "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }



}