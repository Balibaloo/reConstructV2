package com.example.reconstructv2.Fragments.SingleListing;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.BaseAPIResponse;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

public class SingleListingViewModel extends AndroidViewModel {
    private MutableLiveData<SingleListingAPIResponse> listingLiveData;
    private MutableLiveData<BaseAPIResponse> baseAPIResponse;

    private APIRepository apiRepository;

    public SingleListingViewModel(@NonNull Application application) {
        super(application);

        // create a new instance of an APIRepository
        apiRepository = new APIRepository(application);

        // fetch the mutable live data for each required api response type
        listingLiveData = apiRepository.getSingleListingAPIResponseMutableLiveData();
        baseAPIResponse = apiRepository.getBaseAPIResponseMutableLiveData();

    }

    // getters for mytable live data
    public MutableLiveData<SingleListingAPIResponse> getListingLiveData() {
        return listingLiveData;
    }

    public MutableLiveData<BaseAPIResponse> getBaseAPIResponseLiveData() {
        return baseAPIResponse;
    }


    // create a request to reserve listing items
    public void reserveItemsRequest(Context context, ListingFull listing){

        // check if the user is logged in
        if (UserInfo.getIsLoggedIn(context)) {

            // get listing items
            List<ListingItem> listingItemList = listing.getItemList();

            // create empty array for items to reserve
            List<JsonObject> itemsToReserve = new ArrayList<>();


            for (int i = 0; i < listingItemList.size(); i++) {
                
                // get current item
                ListingItem currItem = listingItemList.get(i);

                // check if the item is selected to be reserved
                if (currItem.getIsSelected()) {

                    // create a new jsonObject that holds the itemID of the item to be reserved
                    JsonObject newObj = new JsonObject();
                    newObj.addProperty("itemID",currItem.getItemID());;

                    // add the jsonObject to the list
                    itemsToReserve.add(newObj);
                }
            }

            // send the request to reserve the items
            apiRepository.reserveItems("Bearer " + UserInfo.getToken(context),itemsToReserve);

        } else {
            // if the user is not logged in create a toast
            Toast.makeText(context, "You need to be logged in to reserve items", Toast.LENGTH_SHORT).show();
        }
    }


    // create a request to fetch a listing from the server
    public void fetchListing(Context context, String listingID){

        // check if the user is logged in
        if (UserInfo.getIsLoggedIn(context)) {
            // if the user is logged in
            // fetch the listing with authentication
            apiRepository.getListingAuthenticated("Bearer " + UserInfo.getToken(context), listingID);

        } else {
            // if the user is not logged in
            // fetch the listing without authentication
            apiRepository.getListingNoAuth(listingID);
        }
    }


}
