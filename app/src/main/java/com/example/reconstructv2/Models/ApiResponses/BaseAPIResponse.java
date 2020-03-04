package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("message")
    private String message;

    private Boolean isSuccesfull;


    public BaseAPIResponse(@Nullable Boolean isSuccesfull) {
        this.isSuccesfull = isSuccesfull != null ? isSuccesfull : false;
    }


    public Boolean getIsSuccesfull() {
        return isSuccesfull;
    }

    public void setIsSuccesfull(Boolean succesfull) {
        isSuccesfull = succesfull;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    // method that allows displaying the object as a string
    @NonNull
    @Override
    public String toString() {
        return "message = " + message + "|isSuccesfull = " + isSuccesfull;
    }
}