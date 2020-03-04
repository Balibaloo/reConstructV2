package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.example.reconstructv2.Models.Listing;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingListAPIResponse extends BaseAPIResponse{

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("listings")
    private List<Listing> listings;

    public ListingListAPIResponse(@Nullable Boolean isSuccesfull)
    {
        super(isSuccesfull);
    }

    public List<Listing> getListings() {
        return listings;
    }
}
