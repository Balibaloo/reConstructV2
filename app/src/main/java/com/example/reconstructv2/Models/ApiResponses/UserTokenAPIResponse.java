package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class UserTokenAPIResponse extends BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("userToken")
    private String token;

    @SerializedName("userID")
    private String userID;

    @SerializedName("username")
    private String usenrname;

    public UserTokenAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    // getters

    public String getUserToken() {
        return token;
    }

    public String getUserID() {return userID;}

    public String getUsenrname(){ return usenrname;}
}
