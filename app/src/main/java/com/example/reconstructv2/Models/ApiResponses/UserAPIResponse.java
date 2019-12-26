package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.example.reconstructv2.Models.User;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class UserAPIResponse extends BaseAPIResponse {

    @SerializedName("userProfile")
    private User userProfile;

    public UserAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public User getUserProfile() {
        return userProfile;
    }
}
