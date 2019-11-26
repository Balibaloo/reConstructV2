package com.example.reconstructv2.Fragments.AccountView;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class AccountViewModel extends AndroidViewModel {

    private APIRepository apiRepository;

    private MutableLiveData<ListingListAPIResponse> listingListAPIResponse;

    public AccountViewModel(@NonNull Application application) {
        super(application);

        apiRepository = new APIRepository(application);

        listingListAPIResponse = apiRepository.getListingListAPIResponseMutableLiveData();
    }

    public MutableLiveData<ListingListAPIResponse> getAPIListingResponse() {
        return listingListAPIResponse;
    }


    public void getFrontPageListings() {
        apiRepository.getFrontPageListings();
    }
}
