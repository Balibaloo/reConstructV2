package com.example.reconstructv2.Models.ApiResponses;

import com.example.reconstructv2.Models.ListingFull;
import com.google.gson.annotations.SerializedName;

public class SingleListingAPIResponse extends BaseAPIResponse {
    @SerializedName("listing")
    private ListingFull listing;

    public ListingFull getListing() {
        return listing;
    }
}
