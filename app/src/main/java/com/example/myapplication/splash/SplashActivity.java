package com.example.myapplication.splash;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.homeMenu.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.otp.OTPActivity;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.splash.api.AppVersionApi;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.SessionHelper;
import com.example.myapplication.utilities.Utilities;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    Handler handler;
    PrefManager prefManager;
    ProgressDialog progressDialog;
    SessionHelper sessionHelper;
    Activity activity;
    ArrayList<AppVersionModel> appVersionModels;
    int version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);

        activity=SplashActivity.this;
        prefManager = new PrefManager(this);
        progressDialog = new ProgressDialog(this);
        sessionHelper = new SessionHelper(this);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = Integer.parseInt(info.versionName);


        Log.e(TAG, "onCreate: "+version);

        versionCheck(version);
      //  animation();
    }

    public void animation() {
       // progressDialog.show();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sessionHelper.isLoggedIn()) {
                    Utilities.launchActivity(SplashActivity.this, EngineerHomeActivity.class, true);
                }
                else {
                   // Utilities.launchActivity(SplashActivity.this, OTPActivity.class, true);
                    Utilities.launchActivity(SplashActivity.this, LoginActivity.class, true);
                }
                //Utilities.launchActivity(SplashActivity.this, MainActivity.class, true);


            }
        }, 3000);
    }



    void versionCheck( int version)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            AppVersionApi appVersionApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(AppVersionApi.class);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            appVersionApi.getVersion().
                    enqueue(new Callback<ArrayList<AppVersionModel>>() {
                        @Override
                        public void onResponse(Call<ArrayList<AppVersionModel>> call,
                                               Response<ArrayList<AppVersionModel>> response) {

                            appVersionModels = response.body();
                            // roundarrayList = response.body();

                            Log.e(TAG, appVersionModels.toString());


                            if (version==Integer.parseInt(appVersionModels.get(0).versionno))
                            {
                                new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {

                                        if(sessionHelper.isLoggedIn())
                                        {
                                            Intent mainIntent = new Intent(SplashActivity.this, EngineerHomeActivity.class);
                                            activity.startActivity(mainIntent);
                                            activity.finish();
                                        }else
                                        {
                                            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                                            activity.startActivity(mainIntent);
                                            activity.finish();
                                        }

                                    }
                                }, 3000);

                            }else
                            {
                                versiontDialog();
                            }

                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<ArrayList<AppVersionModel>> call, Throwable t) {

                            progressDialog.dismiss();
                            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.something_went_wrong));
                            Log.d("errorchk",t.getMessage());

                        }
                    });
        }
        else {

            Toast.makeText(activity, getResources().getString(R.string.check_internet), Toast.LENGTH_SHORT).show();
            //Utilities.setError(layout1,layout2,txt_error,getResources().getString(R.string.check_internet));

        }
    }


    private void versiontDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle("Oops! Your application is not updated");

        mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                //Utilities.launchActivity(activity, SplashActivity.class,true);
            }
        });

        mBuilder.setNegativeButton("Cancel", null);
        AlertDialog mDialog = mBuilder.create();
        mDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_rounded_background);
        mDialog.show();
    }
}