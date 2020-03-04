package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ListingIDAPIResponse extends BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("listingID")
    private String listingID;

    public ListingIDAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public String getListingID() {
        return listingID;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "listingID = " + listingID + "|";
    }
}
