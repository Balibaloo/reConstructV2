package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CheckAvailableAPIResponse extends BaseAPIResponse {

    @SerializedName("isAvailable")
    private Boolean is_unused;

    public CheckAvailableAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public Boolean getIs_unused() {
        return is_unused;
    }
}
