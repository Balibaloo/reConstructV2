package com.example.reconstructv2.Fragments.SingleListing;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class SingleListingViewModel extends AndroidViewModel {
    private MutableLiveData<SingleListingAPIResponse> listingLiveData;

    private APIRepository apiRepository;

    public SingleListingViewModel(@NonNull Application application) {
        super(application);
        apiRepository = new APIRepository(application);
        listingLiveData = apiRepository.getSingleListingAPIResponseMutableLiveData();

    }

    public MutableLiveData<SingleListingAPIResponse> getListingLiveData() {
        return listingLiveData;
    }

    public void fetchListing(String listingID){
        apiRepository.getListingNoAuth(listingID);
    }


}
