package com.example.reconstructv2.Fragments.SingleListing;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Models.ListingItem;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

import java.util.ArrayList;
import java.util.List;

public class SingleListingViewModel extends AndroidViewModel {
    private MutableLiveData<SingleListingAPIResponse> listingLiveData;

    private APIRepository apiRepository;

    public SingleListingViewModel(@NonNull Application application) {
        super(application);
        apiRepository = new APIRepository(application);
        listingLiveData = apiRepository.getSingleListingAPIResponseMutableLiveData();

    }

    public void reserveItemsRequest(Context context, ListingFull listing){
        if (UserInfo.getIsLoggedIn(context)) {

            List<ListingItem> listingItemList = listing.getItemList();
            List<String> reservedItemIDs = new ArrayList<>();


            for (int i = 0; i < listingItemList.size(); i++) {
                ListingItem currItem = listingItemList.get(i);
                if (currItem.getIsSelected()) {
                    reservedItemIDs.add(currItem.getItemID());
                }
                
            }

            apiRepository.reserveItems("Bearer " + UserInfo.getToken(context),reservedItemIDs);
        } else {
            Toast.makeText(context, "You need to be logged in to reserve items", Toast.LENGTH_SHORT).show();
        }
    }

    public MutableLiveData<SingleListingAPIResponse> getListingLiveData() {
        return listingLiveData;
    }

    public void fetchListing(Context context, String listingID){
        if (UserInfo.getIsLoggedIn(context)) {
            System.out.println(UserInfo.getToken(context));
            apiRepository.getListingAuthenticated("Bearer " + UserInfo.getToken(context), listingID);
        } else {
            apiRepository.getListingNoAuth(listingID);
        }
    }


}
