package com.example.reconstructv2.Helpers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

public class RequestErrorHandler {

    public static void displayErrorMessage(Context context, Response response){
        if (!(response.errorBody() == null)) {
            try {
                JSONObject responseObject = new JSONObject(response.errorBody().string());
                Toast.makeText(context, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "no error body received", Toast.LENGTH_SHORT).show();
        }
    }
}
