package com.example.reconstructv2.Models.ApiResponses;

import com.example.reconstructv2.Models.User;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class UserAPIResponse extends BaseAPIResponse {

    @SerializedName("userProfile")
    private User userProfile;


    public User getUserProfile() {
        return userProfile;
    }
}
