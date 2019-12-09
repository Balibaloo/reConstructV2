package com.example.reconstructv2.Models.ApiResponses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DesiredItemsAPIResponse extends BaseAPIResponse {
    @SerializedName("desiredItems")
    List<String> desiredItemList;

    public List<String> getDesiredItemList() {
        return desiredItemList;
    }
}
