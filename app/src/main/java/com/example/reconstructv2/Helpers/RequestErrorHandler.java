package com.example.reconstructv2.Helpers;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

// used to log errors when api requests fail
public class RequestErrorHandler {

    public static void displayErrorMessage(Context context, Response response) {

        // check if the responce has an error body
        if (!(response.errorBody() == null)) {
            try {
                // convert the error to a json object
                JSONObject responseObject = new JSONObject(response.errorBody().string());


                if (responseObject.getString("message").equals("Token Not Valid")) {
                        Toast.makeText(context, "Please Log In Again", Toast.LENGTH_SHORT).show();

                } else {
                    // display a toast with the message from the json object
                    Toast.makeText(context, responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

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
