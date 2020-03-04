package com.example.reconstructv2.Models.ApiResponses;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DesiredItemsAPIResponse extends BaseAPIResponse {

    //@SerializedName annotation indicates the annotated member should be serialized to JSON with the provided name value as its field name.

    @SerializedName("desiredItems")
    List<String> desiredItemList;

    public DesiredItemsAPIResponse(@Nullable Boolean isSuccesfull) {
        super(isSuccesfull);
    }

    public List<String> getDesiredItemList() {
        return desiredItemList;
    }
}
