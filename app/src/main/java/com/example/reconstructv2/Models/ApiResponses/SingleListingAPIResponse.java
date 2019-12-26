package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.example.reconstructv2.Models.ListingFull;
import com.google.gson.annotations.SerializedName;

public class SingleListingAPIResponse extends BaseAPIResponse {
    @SerializedName("listing")
    private ListingFull listing;

    public SingleListingAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public ListingFull getListing() {
        return listing;
    }
}
