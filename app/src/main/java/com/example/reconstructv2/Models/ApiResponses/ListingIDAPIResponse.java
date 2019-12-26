package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ListingIDAPIResponse extends BaseAPIResponse {
    @SerializedName("listingID")
    private String listingID;

    public ListingIDAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public String getListingID() {
        return listingID;
    }
}
