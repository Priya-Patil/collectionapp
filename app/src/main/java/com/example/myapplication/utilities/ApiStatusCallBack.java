package com.example.myapplication.utilities;

import com.androidnetworking.error.ANError;

public interface ApiStatusCallBack<T> {
    public void onSuccess(T t);
    public void onError(ANError anError);
    public void onUnknownError(Exception e);
}
