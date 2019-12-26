package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UserTokenAPIResponse extends BaseAPIResponse {

    @SerializedName("userToken")
    private String token;

    @SerializedName("userID")
    private String userID;

    public UserTokenAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public String getUserToken() {
        return token;
    }

    public String getUserID() {return userID;}
}
