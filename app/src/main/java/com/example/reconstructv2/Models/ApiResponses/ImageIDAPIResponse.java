package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class ImageIDAPIResponse extends BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("imageID")
    String imageID;

    @SerializedName("itemPos")
    int itemPos;

    public ImageIDAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public int getItemPos() {
        return itemPos;
    }

    public String getImageID() {
        return imageID;
    }
}
