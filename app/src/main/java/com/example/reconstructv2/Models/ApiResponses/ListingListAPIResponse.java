package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.example.reconstructv2.Models.Listing;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingListAPIResponse extends BaseAPIResponse{
    @SerializedName("listings")
    private List<Listing> listings;

    public ListingListAPIResponse(@Nullable Boolean isSuccesfull)
    {
        super(isSuccesfull);
        System.out.println("child class received = " + isSuccesfull);
    }

    public List<Listing> getListings() {
        return listings;
    }
}
