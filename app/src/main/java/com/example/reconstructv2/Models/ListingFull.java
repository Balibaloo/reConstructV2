package com.example.reconstructv2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingFull extends Listing {

    @SerializedName("itemList")
    private List<ListingItem> itemList;

    public ListingFull(String listingID, String authorID, String title, String body, String post_date, String end_date, String location, Boolean isActive, String imageID, List<ListingItem> itemList) {
        super(listingID, authorID, title, body, post_date, end_date, location, isActive, imageID);
        this.itemList = itemList;
    }

    public List<ListingItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ListingItem> itemList) {
        this.itemList = itemList;
    }

    public void addListingItemImage(Integer itemIndex,String imageID){
        ListingItem tempitem = this.itemList.get(itemIndex);
        System.arraycopy(new String[]{imageID},0,tempitem.getImageIDArray(),0,1);
        this.itemList.set(itemIndex,tempitem);
    }
}
