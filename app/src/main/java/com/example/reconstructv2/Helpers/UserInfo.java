package com.example.reconstructv2.Helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

// used to store and retreive user data

public class UserInfo {
    // key for the shared preferences
    private final static String SHARED_PREF_NAME = "UserInfo";

    // keys for individual values
    private final static String TOKEN_KEY = "userToken";
    private final static String USERID_KEY = "UserID";
    private final static String IS_LOGGED_IN_KEY =  "isLoggedIn";


    public static String getToken(Context c){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // return the value of the user token
        return prefs.getString(TOKEN_KEY,"");
    }

    public static String getSelfUserID(Context c){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // return the value of the user's ID
        return prefs.getString(USERID_KEY,"");
    }

    public static Boolean getIsLoggedIn(Context c){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // return the value of is_logged_in
        return prefs.getBoolean(IS_LOGGED_IN_KEY, false);
    }

    public static void setToken(Context c,String token){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // open shared preferences with an editor
        SharedPreferences.Editor editor = prefs.edit();

        // write new value for token
        editor.putString(TOKEN_KEY, token);

        // save changes
        editor.apply();
    }

    public static void setSelfUserID(Context c ,String userID){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // open shared preferences with an editor
        SharedPreferences.Editor editor = prefs.edit();

        // write new value for token
        editor.putString(USERID_KEY, userID);
        
        // save changes
        editor.apply();
    }

    


    public static void setIsLoggedIn(Context c ,Boolean isLoggedIn){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // open shared preferences with an editor
        SharedPreferences.Editor editor = prefs.edit();

        // write new value for isLoggedIn
        editor.putBoolean(IS_LOGGED_IN_KEY, isLoggedIn);

        // save changes
        editor.apply();
    }

    

    public static void logOut(Context c){

        // get shared preference object
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // open shared preferences with an editor
        SharedPreferences.Editor editor = prefs.edit();

        // set is logged in as false
        editor.putBoolean(IS_LOGGED_IN_KEY, false);

        // set the user acces token to null
        editor.putString(TOKEN_KEY,null);

        // set the user id to null
        editor.putString(USERID_KEY,null);

        // save changes
        editor.apply();

        // display to the user that they logged out
        Toast.makeText(c, "Succesfully Logged Out", Toast.LENGTH_SHORT).show();
    }

}
