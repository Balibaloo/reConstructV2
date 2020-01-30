package com.example.reconstructv2.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListingItem implements Serializable {

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
    private String[] tagList;

    @SerializedName("imageArray")
    private String[] imageIDArray;

    private Boolean isSelected;

    public ListingItem(String itemID, @Nullable String name, String description, Boolean isAvailable, String takenByUserID, String[] tagList, String[] imageIDArray) {
        this.ItemID = itemID;
        this.name = (name == null ? "item name" : name);
        this.description = description != null ? description : "description";
        this.isAvailable = isAvailable != null ? isAvailable : true;
        this.takenByUserID = takenByUserID;
        this.tagList = tagList != null ? tagList : new String[]{"defaultImage"};
        this.imageIDArray = imageIDArray != null ? imageIDArray : new String[]{"temp id"};
        this.isSelected = false;
    }


    public String getItemID() {
        return ItemID;
    }

    public String[] getImageIDArray() {
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

    public String[] getTagList() {
        return tagList;
    }

    public void toggleIsSelected() {
        isSelected = !this.getIsSelected();
    }

    public Boolean getIsSelected() {
        if (isSelected == null){isSelected = false;}
        return isSelected;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public void setTakenByUserID(String takenByUserID) {
        this.takenByUserID = takenByUserID;
    }

    public void setTagList(String[] tagList) {
        this.tagList = tagList;
    }

    public void setImageIDArray(String[] imageIDArray) {
        this.imageIDArray = imageIDArray;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

}
