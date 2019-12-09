package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class BaseAPIResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("test_var")
    private String testVariable;


    public String getMessage() {
        return message;
    }

    public String getTestVariable() {
        return testVariable;
    }
}