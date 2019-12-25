package com.example.reconstructv2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingFull extends Listing {

    @SerializedName("itemList")
    private List<ListingItem> itemList;

    public ListingFull(String listingID, String authorID, String title, String body, String post_date, String end_date, String location, Boolean isActive, String imageID) {
        super(listingID, authorID, title, body, post_date, end_date, location, isActive, imageID);
    }

    public List<ListingItem> getItemList() {
        return itemList;
    }

}
