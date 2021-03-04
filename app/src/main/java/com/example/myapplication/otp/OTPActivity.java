 package com.example.myapplication.otp;

 import android.Manifest;
 import android.app.Activity;
 import android.app.ProgressDialog;
 import android.content.BroadcastReceiver;
 import android.content.Context;
 import android.content.Intent;
 import android.content.IntentFilter;
 import android.graphics.Color;
 import android.media.MediaPlayer;
 import android.os.Bundle;
 import android.os.CountDownTimer;
 import android.text.Spannable;
 import android.text.SpannableString;
 import android.text.method.LinkMovementMethod;
 import android.text.style.ClickableSpan;
 import android.text.style.ForegroundColorSpan;
 import android.util.Log;
 import android.view.View;
 import android.view.WindowManager;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.databinding.DataBindingUtil;
 import androidx.localbroadcastmanager.content.LocalBroadcastManager;


 import com.androidnetworking.error.ANError;
 import com.example.myapplication.R;
 import com.example.myapplication.databinding.ActivityOTPBinding;
 import com.example.myapplication.engineer.EngineerHomeActivity;
 import com.example.myapplication.homeMenu.MainActivity;
 import com.example.myapplication.otp.api.OTPApi;
 import com.example.myapplication.otp.model.OTPResponce;
 import com.example.myapplication.retrofit.RetrofitClientInstance;
 import com.example.myapplication.services.OTPServices;
 import com.example.myapplication.utilities.ApiStatusCallBack;
 import com.example.myapplication.utilities.PrefManager;
 import com.example.myapplication.utilities.SessionHelper;
 import com.example.myapplication.utilities.Utilities;
 import com.karumi.dexter.Dexter;
 import com.karumi.dexter.MultiplePermissionsReport;
 import com.karumi.dexter.PermissionToken;
 import com.karumi.dexter.listener.DexterError;
 import com.karumi.dexter.listener.PermissionRequest;
 import com.karumi.dexter.listener.PermissionRequestErrorListener;
 import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


 import java.util.List;
 import java.util.Locale;
 import java.util.Random;

 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.Response;


 public class OTPActivity extends AppCompatActivity {

     ActivityOTPBinding binding;
    private CountDownTimer mCountDownTimer;
    private long mTimeLeftInMillis;

    //String OTP="1234";
    String OTP="";
    Activity activity;

    ProgressDialog progressDialog;
    PrefManager prefManager;
    SessionHelper sessionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_o_t_p);

        activity=OTPActivity.this;
        progressDialog=new ProgressDialog(OTPActivity.this);
        prefManager=new PrefManager(OTPActivity.this);
        sessionHelper=new SessionHelper(OTPActivity.this);

        requestPermission();
        setupHyperlink();


        binding.submit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isValidPhoneNumber(binding.etMobile.getText().toString()))
                {
                  //  Utility.launchActivity(OTPActivity.this, HomeActivity.class, true);

                    OTP = "123456"; //GenerateRandomNumber(6);
                    Log.e( "onClick: ",  OTP);

                    String message = "Thank you for visiting Wheel alignment app! \n Your OTP Is :" + OTP;


                    if (binding.checkBoxAccept.isChecked()) {
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);
                        timerForOtp(binding.etMobile.getText().toString(), message);

                    }
                    else
                    {
                        Toast.makeText(OTPActivity.this, "Please agree terms and condition to proceed ", Toast.LENGTH_SHORT).show();
                    }

               }
                else {
                    Toast.makeText(OTPActivity.this, "Enter proper mobile number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.submit2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(binding.etOtp.getText().toString().equals(""))
                            {
                                Toast.makeText(OTPActivity.this, "Enter otp", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                if (binding.etOtp.getText().toString().equals(OTP))
                                {
                                    if (binding.etOtp.getText().toString().equals("123456"))
                                    {
                                       // sessionHelper.setLogin(true);
                                        checkLogin(binding.etMobile.getText().toString());
                                    }
                                } else {
                                    Toast.makeText(OTPActivity.this, "Enter proper otp", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    }


     private void setupHyperlink() {

    //     binding.tvHyperLink.setMovementMethod(LinkMovementMethod.getInstance());



         binding.tvHyperLink.setMovementMethod(LinkMovementMethod.getInstance());


         Spannable spans = (Spannable)  binding.tvHyperLink.getText();
         ClickableSpan clickSpan = new ClickableSpan() {

             @Override
             public void onClick(View widget)
             {

              //   Utilities.launchActivity(activity, PDFActivity.class, false);

             }
         };
        // spans.setSpan(clickSpan, 0, spans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
         spans.setSpan(clickSpan, 0, spans.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
     }




    private void updateCountDownText(long millisUntilFinished) {
        int minutes = (int) (millisUntilFinished / 1000) / 60;
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%d:%d", minutes, seconds);
        //text_otp_expire.setText(timeLeftFormatted);
        Spannable WordtoSpan = new SpannableString("OTP expire after "+timeLeftFormatted+"  ");
        WordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 17, 21,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.textOtpExpire.setText(WordtoSpan);

        //text_otp_expire.setText("OTP expire after  "+timeLeftFormatted+"  Seconds");

    }

    private void timerForOtp(String mobileNumber, String message) {

        binding.tvSmsRecv.setVisibility(View.GONE);
        SpannableString span = new SpannableString("Didn't receive SMS ? Resend");
        span.setSpan(new ForegroundColorSpan(Color.GRAY), 21, 27, 0);
        binding.tvSmsRecv.setTextColor(Color.parseColor("#48494b"));

        mCountDownTimer = new CountDownTimer(120000, 1000) {
            // mCountDownTimer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {

                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText(millisUntilFinished);
                //120 sec=120000ms
                if ((millisUntilFinished / 1000) == 117) {


                    //sendOTP(mobileNumber,message);

                }
                else {

                  //  sendCodeButton.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() {
                //sendOTP(mobileNumber, message);
                //Toast.makeText(AuthenticationActivity.this,"Time Out!! Resend OTP",Toast.LENGTH_LONG).show();
                SpannableString span = new SpannableString("Didn't receive SMS ? Resend");
                span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange)), 21, 27, 0);
                binding.tvSmsRecv.setText(span, TextView.BufferType.SPANNABLE);
                binding.tvSmsRecv.setVisibility(View.VISIBLE);
                //tv_sms_recv.setTextColor(Color.parseColor("#002266"));
                binding.tvSmsRecv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        OTP = GenerateRandomNumber(6);
                        Log.e("ChkOTP",""+OTP);
                        String message2 = "Thank you for visiting app! \n Your OTP Is :" + OTP;
                        binding.layout1.setVisibility(View.GONE);
                        binding.layout2.setVisibility(View.VISIBLE);
                        timerForOtp(mobileNumber,message2);

                    }
                });

            }

        }.start();
    }

    public static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{10}$";
        return mobile.matches(regEx);
    }

    String GenerateRandomNumber(int charLength) {
        return String.valueOf(charLength < 1 ? 0 : new Random()
                .nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
                + (int) Math.pow(10, charLength - 1));
    }

    void checkLogin(String mobile)
    {
        if(Utilities.isNetworkAvailable(activity))
        {
            OTPApi loginApi = RetrofitClientInstance.getRetrofitInstanceServer().
                    create(OTPApi.class);

            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            // pbLoading.setProgressStyle(R.id.abbreviationsBar);
            progressDialog.show();
            loginApi.checkLogin(mobile).
                    enqueue(new Callback<OTPResponce>() {

                        @Override
                        public void onResponse(Call<OTPResponce> call, Response<OTPResponce> response) {

                              OTPResponce otpResponce = response.body();

                                sessionHelper.setLogin(true);//true
                                Log.e("otpResponce: ", String.valueOf(otpResponce.id));
                                prefManager.setUserid(otpResponce.id);
                                prefManager.setMobile(otpResponce.mobile);
                                progressDialog.dismiss();
                          /*  Intent intent = new Intent(getApplicationContext(), Select_Nav_Menu.class);
                            startActivity(intent);*/
                                Utilities.launchActivity(OTPActivity.this, EngineerHomeActivity.class, true);
                        }

                        @Override
                        public void onFailure(Call<OTPResponce> call, Throwable t) {

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

    void sendOTP(String mobileNumber, String message) {
        OTPServices.getInstance(OTPActivity.this).SendOTP(mobileNumber, message, new ApiStatusCallBack() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(OTPActivity.this, "sent otp", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(ANError anError) {
                Log.e( "onError: ", anError.toString());
             //   Toast.makeText(OTPActivity.this, "Failed "+anError.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onUnknownError(Exception e) {
               // Toast.makeText(OTPActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                binding.etOtp.setText(message);
                Log.e( "onReceive: ", message );
                // message is the fetching OTP
            }
        }
    };

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.SEND_SMS)
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
                        Toast.makeText(getApplicationContext(), "Error occurred! "+error, Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


}