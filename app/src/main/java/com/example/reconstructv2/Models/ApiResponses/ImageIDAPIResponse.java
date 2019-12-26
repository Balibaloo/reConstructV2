package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ImageIDAPIResponse extends BaseAPIResponse {
    @SerializedName("imageID")
    String imageID;

    public ImageIDAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public String getImageID() {
        return imageID;
    }
}
