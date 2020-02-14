package com.example.reconstructv2.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class UserInfo {
    private final static String SHARED_PREF_NAME = "";

    private final static String TOKEN_KEY = "userToken";
    private final static String SELF_USERID_KEY = "selfUserID";
    private final static String IS_LOGGED_IN_KEY =  "isLoggedIn";

    public static void setToken(Context c,String token){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

    public static String getToken(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY,"");
    }

    public static void setSelfUserID(Context c ,String userID){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SELF_USERID_KEY, userID);
        editor.apply();
    }

    public static String getSelfUserID(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(SELF_USERID_KEY,"");
    }


    public static void setIsLoggedIn(Context c ,Boolean isLoggedIn){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IS_LOGGED_IN_KEY, isLoggedIn);
        editor.apply();
    }

    public static Boolean getIsLoggedIn(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(IS_LOGGED_IN_KEY, false);
    }

    public static void logOut(Context c){
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(IS_LOGGED_IN_KEY, false);
        editor.putString(TOKEN_KEY,null);
        editor.putString(SELF_USERID_KEY,null);
        editor.apply();

        Toast.makeText(c, "Succesfully Logged Out", Toast.LENGTH_SHORT).show();
    }

}
