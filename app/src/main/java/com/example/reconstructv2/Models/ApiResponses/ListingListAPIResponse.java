package com.example.reconstructv2.Models.ApiResponses;

import com.example.reconstructv2.Models.Listing;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingListAPIResponse  {
    @SerializedName("listings")
    private List<Listing> listings;

    public List<Listing> getListings() {
        return listings;
    }
}
