package com.example.reconstructv2.Fragments.Home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.ListingListAPIResponse;
import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ListingRepository repository;
    private LiveData<List<Listing>> allListings;

    private APIRepository apiRepository;

    private MutableLiveData<ListingListAPIResponse> listingListAPIResponse;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new ListingRepository(application);
        allListings = repository.getAllListings();

        apiRepository = new APIRepository(application);
        listingListAPIResponse = apiRepository.getListingListAPIResponseMutableLiveData();

    }



    public LiveData<List<Listing>> getAllListings() {
        return allListings;
    }

    public MutableLiveData<ListingListAPIResponse> getListingListAPIResponse(){
        return listingListAPIResponse;
    }

    public void insert(Listing listing) {
        repository.insert(listing);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void insertList(List<Listing> listings) {
        for (Listing listing : listings) {
            repository.insert(listing);
        }
    }



    public void fetchFrontPageListingsRequest(){
        apiRepository.getFrontPageListings();
    }
}
