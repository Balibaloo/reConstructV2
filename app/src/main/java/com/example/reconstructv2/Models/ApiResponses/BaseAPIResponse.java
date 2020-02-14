package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class BaseAPIResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("test_var")
    private String testVariable;

    private Boolean isSuccesfull;

    public String getMessage() {
        return message;
    }

    public String getTestVariable() {
        return testVariable;
    }

    public BaseAPIResponse(@Nullable Boolean isSuccesfull) {
        System.out.println("received is success = " + isSuccesfull);

        if (isSuccesfull ==  null){
            this.isSuccesfull = true;
        } else {
            this.isSuccesfull = isSuccesfull;
        }

    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsSuccesfull(Boolean succesfull) {
        isSuccesfull = succesfull;
    }

    public Boolean getIsSuccesfull() {
        return isSuccesfull;
    }

    @NonNull
    @Override
    public String toString() {
        return "testVar = " + testVariable + " |message = " + message+ "|isSuccesfull = " + isSuccesfull;
    }
}