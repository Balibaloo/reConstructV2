package com.example.reconstructv2.Models;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.reconstructv2.R;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListingItem implements Serializable {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

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

    @SerializedName("images")
    private List<String> imageIDArray;

    private Boolean isSelected;

    public ListingItem(String itemID, @Nullable String name, String description, Boolean isAvailable, String takenByUserID, List<String> imageIDArray) {
        this.ItemID = itemID != null ? itemID : "defaultID";
        this.name = (name != null ? name : "");
        this.description = description != null ? description : "";
        this.isAvailable = isAvailable != null ? isAvailable : true;
        this.takenByUserID = takenByUserID;

        if (imageIDArray == null){
            this.imageIDArray = new ArrayList<>();
        } else {
            this.imageIDArray = imageIDArray;
        }
        this.isSelected = false;
    }


    public String getItemID() {
        return ItemID;
    }

    public List<String> getImageIDArray() {
        return imageIDArray;
    }

    public void setImageToArray(Integer pos ,String imageID){
        if (this.imageIDArray.isEmpty()){
            this.imageIDArray.add(imageID);
        } else {
            this.imageIDArray.set(pos, imageID);
        }
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

    public void setImageIDArray(List<String> imageIDArray) {
        this.imageIDArray = imageIDArray;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" + ItemID + "," + name + "," + description + "}";
    }
}
