package com.example.reconstructv2.Fragments.AccountManagement.RecentlyViewedListings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;


public class recentlyViewedViewModel extends AndroidViewModel {
    private APIRepository apiRepository;

    private MutableLiveData<ListingListAPIResponse> listingListAPIResponse;

    public recentlyViewedViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);

        listingListAPIResponse = apiRepository.getListingListAPIResponseMutableLiveData();
    }

    // geter for listingListAPIResponse
    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponse() {
        return listingListAPIResponse;
    }

    // send the request to the server
    public void fetchRecentlyViewedListingsRequest() {
        apiRepository.getRecentListings(UserInfo.getAuthenticationHeader(getApplication()),0);
    }
}