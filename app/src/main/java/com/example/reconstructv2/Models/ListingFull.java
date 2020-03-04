package com.example.reconstructv2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListingFull extends Listing {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("itemList")
    private List<ListingItem> itemList;

    public ListingFull(String listingID, String authorID, String title, String body, String post_date, String end_date, Boolean isActive, String imageID, List<ListingItem> itemList, Double loc_lon,Double loc_lat) {
        super(listingID, authorID, title, body, post_date, end_date, loc_lat,loc_lon,isActive, imageID);

        this.itemList = itemList != null ? itemList : new ArrayList<ListingItem>();
    }

    public List<ListingItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ListingItem> itemList) {
        this.itemList = itemList;
    }

    public void setListingItemImage(Integer itemIndex, Integer imageIndex, String imageID){
        this.itemList.get(itemIndex).setImageToArray(imageIndex, imageID);
    }
}
