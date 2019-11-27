package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

public class ImageIDAPIResponse extends BaseAPIResponse {
    @SerializedName("imageID")
    String imageID;

    public String getImageID() {
        return imageID;
    }
}
