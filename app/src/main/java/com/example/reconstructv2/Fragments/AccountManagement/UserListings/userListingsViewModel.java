package com.example.reconstructv2.Fragments.AccountManagement.UserListings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class userListingsViewModel extends AndroidViewModel {
    private APIRepository apiRepository;

    private MutableLiveData<ListingListAPIResponse> listingListAPIResponse;

    public userListingsViewModel(@NonNull Application application) {
        super(application);
        apiRepository = new APIRepository(application);
        listingListAPIResponse = apiRepository.getListingListAPIResponseMutableLiveData();
    }

    // getter
    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponse() {
        return listingListAPIResponse;
    }

    // send requests
    public void sendFetchUserListingsRequest(Integer pageNumber) {
        apiRepository.getUserListings(UserInfo.getSelfUserID(getApplication()),pageNumber);
    }
}