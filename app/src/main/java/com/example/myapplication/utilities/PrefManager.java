package com.example.myapplication.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    public static int PRIVATE_MODE = 0;
    // Shared preferences file name
    public static final String PREF_NAME = "badsha";
    // All Shared Preferences Keys
    private static final String Mobile = "mobile";
    private static final String Cityid = "cityid";
    private static final String Userid = "Userid";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String isPaid = "isPaid";
    private static final String Name = "name";
    private static final String vname = "vname";
    private static final String aid = "aid";
    private static final String rid = "rid";
    private static final String profileimg = "profileimg";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //
    public void setMobile(String S) {
        editor.putString(Mobile, S);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(Mobile, null);
    }

 //
    public void setCityide(String S) {
        editor.putString(Cityid, S);
        editor.commit();
    }

    public String getCityid() {
        return pref.getString(Cityid, null);
    }


    public void setUserid(int S) {
        editor.putInt(Userid, S);
        editor.commit();
    }

    public int getUserid() {
        return pref.getInt(Userid, 0);
    }


    public void setisPaid(String S) {
        editor.putString(isPaid, S);
        editor.commit();
    }

    public String getisPaid() {
        return pref.getString(isPaid, null);
    }



    public void setAttendance(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean getAttendance() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false);
    }



    //
    public void setName(String S) {
        editor.putString(Name, S);
        editor.commit();
    }

    public String getName() {
        return pref.getString(Name, null);
    }


    //
    public void setvname(String S) {
        editor.putString(vname, S);
        editor.commit();
    }

    public String getvname() {
        return pref.getString(vname, "Vehicle name");
    }



    public void setaid(int S) {
        editor.putInt(aid, S);
        editor.commit();
    }

    public int getaid() {
        return pref.getInt(aid, 0);
    }



    public void setrid(int S) {
        editor.putInt(rid, S);
        editor.commit();
    }

    public int getrid() {
        return pref.getInt(rid, 0);
    }



    //
    public void setprofileimg(String S) {
        editor.putString(profileimg, S);
        editor.commit();
    }

    public String getprofileimg() {
        return pref.getString(profileimg, "Vehicle name");
    }


}