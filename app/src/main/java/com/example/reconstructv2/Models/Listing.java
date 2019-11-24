package com.example.reconstructv2.Models;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "listing_table")
public class Listing implements Serializable {
    @PrimaryKey
    @NonNull
    private String listingID;
    private String authorID;
    private String title;
    private String body;
    private String post_date;
    private String end_date;
    private String location;

    @Ignore
    private Bitmap image;

    @ColumnInfo(name = "isActive")
    private Boolean isActive;

    public Listing(String listingID, String authorID, String title, String body, String post_date, String end_date, String location, Boolean isActive) {
        this.listingID = listingID;
        this.authorID = authorID;
        this.title = title;
        this.body = body;
        this.post_date = post_date;
        this.end_date = end_date;
        this.location = location;
        this.isActive = isActive;
    }


    public String getListingID() {
        return listingID;
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

    public String getPost_date() {
        return post_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getActive() {
        return isActive;
    }
}