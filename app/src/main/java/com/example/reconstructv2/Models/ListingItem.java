package com.example.reconstructv2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListingItem {

    @SerializedName("listingItemID")
    private String ItemID;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("isAvailable")
    private Boolean isAvailable;

    @SerializedName("takenByUserID")
    private String takenByUserID;

    @SerializedName("tagList")
    private List<String> tagList;

    @SerializedName("imageArray")
    private List<String> imageIDArray;

    private Boolean isSelected;

    public ListingItem(String itemID, String name, String description, Boolean isAvailable, String takenByUserID, List<String> tagList, List<String> imageIDArray) {
        ItemID = itemID;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.takenByUserID = takenByUserID;
        this.tagList = tagList;
        this.imageIDArray = imageIDArray;
        this.isSelected = false;
    }

    public String getItemID() {
        return ItemID;
    }

    public List<String> getImageIDArray() {
        return imageIDArray;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public String getTakenByUserID() {
        return takenByUserID;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void toggleIsSelected() {
        isSelected = !this.getIsSelected();
    }

    public Boolean getIsSelected() {
        if (isSelected == null){isSelected = false;}
        return isSelected;
    }
}
