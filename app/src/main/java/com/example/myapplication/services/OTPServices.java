package com.example.myapplication.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.myapplication.utilities.ApiStatusCallBack;


public class OTPServices {

    private static final String TAG = OTPServices.class.getSimpleName();

    private Context context;

    public OTPServices(Context context) {
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    private static OTPServices instance;

    public static OTPServices getInstance(Context context) {
        if (instance == null) {
            instance = new OTPServices(context);
        }
        return instance;
    }

    public void SendOTP(String mobile, String message, final ApiStatusCallBack apiStatusCallBack) {
        try {
            AndroidNetworking.get("http://weberleads.in/http-tokenkeyapi.php?authentic-key=35374353776f726c643633301606560098&senderid=CITYWT&route=2&number="+mobile+"&message="+message)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsString(new StringRequestListener() {
                        @Override
                        public void onResponse(String response) {
                            apiStatusCallBack.onSuccess(response);
                        }

                        @Override
                        public void onError(ANError anError) {
                            apiStatusCallBack.onError(anError);
                        }
                    });
        } catch (Exception ex) {
            Log.e("onUnknownError", "" + ex);
            apiStatusCallBack.onUnknownError(ex);
        }
    }
}
