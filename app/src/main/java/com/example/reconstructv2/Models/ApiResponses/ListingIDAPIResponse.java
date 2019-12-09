package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class ListingIDAPIResponse extends BaseAPIResponse {
    @SerializedName("listingID")
    private String listingID;

    public String getListingID() {
        return listingID;
    }
}
