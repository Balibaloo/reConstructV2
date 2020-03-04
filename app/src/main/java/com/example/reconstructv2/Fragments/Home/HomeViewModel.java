package com.example.reconstructv2.Fragments.Home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class HomeViewModel extends AndroidViewModel {


    private APIRepository apiRepository;

    private MutableLiveData<ListingListAPIResponse> listingListAPIResponse;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);
        listingListAPIResponse = apiRepository.getListingListAPIResponseMutableLiveData();

    }

    // getter

    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponse() {
        return listingListAPIResponse;
    }


    // send requests to server
    public void sendSearchRequest(String searchQuery, Integer pageNum) {
        apiRepository.getFilteredListings(searchQuery, pageNum);
    }

    public void sendFrontPageListingsRequest() {
        apiRepository.getFrontPageListings();
    }
}
