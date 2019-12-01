package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class UserTokenAPIResponse extends BaseAPIResponse {

    @SerializedName("user_token")
    private String token;

    @SerializedName("userID")
    private String userID;

    public String getUserToken() {
        return token;
    }

    public String getUserID() {return userID;}
}
