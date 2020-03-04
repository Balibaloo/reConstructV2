package com.example.reconstructv2.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class Listing implements Serializable {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("listingID")
    private String listingID;

    @SerializedName("authorID")
    private String authorID;

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("post_date")
    private String post_date;

    @SerializedName("end_date")
    private String end_date;

    @SerializedName("location_lat")
    private Double location_lat;

    @SerializedName("location_lon")
    private Double location_lon;

    @SerializedName("mainImageID")
    private String mainImageID;

    @SerializedName("isActive")
    private Boolean isActive;

    public Listing(String listingID, String authorID, String title, String body, String post_date, String end_date, Double location_lat, Double location_lon, Boolean isActive, String mainImageID) {
        this.listingID = listingID;
        this.authorID = authorID;
        this.title = title != null ? title : "";
        this.body = body != null ? body : "";
        this.post_date = post_date;
        this.end_date = end_date;
        this.location_lat = location_lat != null ? location_lat : 0.0;
        this.location_lon = location_lon != null ? location_lon : 0.0;
        this.isActive = isActive == null ? true : isActive;
        this.mainImageID = mainImageID;
    }

    // Getters

    public String getMainImageID() {
        return mainImageID;
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

    public Double getLocation_lat() {
        return location_lat;
    }

    public Double getLocation_lon() {
        return location_lon;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    // Setters

    public void setListingID(@NonNull String listingID) {
        this.listingID = listingID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setMainImageID(String mainImageID) {
        this.mainImageID = mainImageID;
    }

    public void setLocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }

    public void setLocation_lon(Double location_lon) {
        this.location_lon = location_lon;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}