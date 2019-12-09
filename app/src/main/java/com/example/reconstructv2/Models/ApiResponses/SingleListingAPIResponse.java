package com.example.reconstructv2.Models.ApiResponses;

import com.example.reconstructv2.Models.Listing;
import com.google.gson.annotations.SerializedName;

public class SingleListingAPIResponse extends BaseAPIResponse {
    @SerializedName("listing")
    private Listing listing;

    public Listing getListing() {
        return listing;
    }
}
