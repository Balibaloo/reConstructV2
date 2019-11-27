package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class CheckAvailableAPIResponse extends BaseAPIResponse {

    @SerializedName("is_unused")
    private Boolean is_unused;

    public Boolean getIs_unused() {
        return is_unused;
    }
}
