package com.example.reconstructv2.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListingToCreate implements Serializable {
    private String authorID;
    private String title;
    private String body;
    private String end_date;
    private String location;
    private String mainImageID;

    @SerializedName("itemList")
    private List<ListingItem> itemList;


    public ListingToCreate(String authorID, String title, String body, String end_date, String location, String mainImageID) {
        this.authorID = authorID;
        this.title = title;
        this.body = body;
        this.end_date = end_date;
        this.location = location;
        this.mainImageID = mainImageID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getLocation() {
        return location;
    }

    public String getMainImageID() {
        return mainImageID;
    }
    public List<ListingItem> getItemList() {
        return itemList;
    }
}
