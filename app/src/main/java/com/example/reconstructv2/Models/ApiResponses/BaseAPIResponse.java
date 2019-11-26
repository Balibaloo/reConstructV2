package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class BaseAPIResponse {
    @SerializedName("message")
    private String message;


    public String getMessage() {
        return message;
    }
}