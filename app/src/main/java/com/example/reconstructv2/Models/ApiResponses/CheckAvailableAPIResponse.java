package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class CheckAvailableAPIResponse extends BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("isAvailable")
    private Boolean is_unused;

    public CheckAvailableAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public Boolean getIs_unused() {
        return is_unused;
    }
}
