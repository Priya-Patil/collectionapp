package com.example.myapplication.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.engineer.EngineerHomeActivity;
import com.example.myapplication.login.api.LoginApi;
import com.example.myapplication.login.model.LoginResponce;
import com.example.myapplication.otp.OTPActivity;
import com.example.myapplication.retrofit.RetrofitClientInstance;
import com.example.myapplication.splash.SplashActivity;
import com.example.myapplication.utilities.PrefManager;
import com.example.myapplication.utilities.SessionHelper;
import com.example.myapplication.utilities.Utilities;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.otp.OTPActivity.isValidPhoneNumber;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    Activity activity;
    SessionHelper sessionHelper;

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        activity = LoginActivity.this;
        progressDialog = new ProgressDialog(activity);
        sessionHelper = new SessionHelper(activity);
        prefManager = new PrefManager(activity);

        binding.txtSend.setOnClickListener(this);
        binding.btnReg.setOnClickListener(this);
        binding.btnForgotpass.setOnClickListener(this);
        binding.btnHome.setOnClickListener(this);

    }

    void checkLogin(String password, String pass) {

        if (Utilities.isNetworkAvailable(activity)){
            LoginApi loginApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(LoginApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            loginApi.checkLogin(password, pass).
                enqueue(new Callback<ArrayList<LoginResponce>>() {
                    @Override
                    public void onResponse(Call<ArrayList<LoginResponce>> call, Response<ArrayList<LoginResponce>> response) {

                        ArrayList<LoginResponce> loginResponce = response.body();
                   //     Log.e( "onResponse: ", loginResponce.toString());
                        progressDialog.dismiss();

                        if(loginResponce.size()==0)
                        {
                            Utilities.messageDialog(activity,"Invalid Login");
                        }
                        else {
                            sessionHelper.setLogin(true);
                            prefManager.setUserid(loginResponce.get(0).id);
                            prefManager.setName(loginResponce.get(0).name);
                            prefManager.setprofileimg(loginResponce.get(0).img);
                            Utilities.launchActivity(activity, EngineerHomeActivity.class, true);
                        }

                    }
                    @Override
                    public void onFailure(Call<ArrayList<LoginResponce>> call, Throwable t) {

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
    public void onClick(View v) {

        switch (v.getId()) {
            default:
                break;

            case R.id.txt_send:
                if(binding.edtMobileno.getText().toString().equals("")|| binding.etPass.getText().toString().equals(""))
                {
                    Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (isValidPhoneNumber(binding.edtMobileno.getText().toString()))
                    {
                        checkLogin(binding.edtMobileno.getText().toString(), binding.etPass.getText().toString());
                    }
                    else {
                        Toast.makeText(activity, "Mobile number must be 10 digits", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


}