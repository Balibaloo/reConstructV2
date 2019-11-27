package com.example.reconstructv2.Fragments.Home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.reconstructv2.Models.Listing;
import com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ListingRepository repository;
    private LiveData<List<Listing>> allListings;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new ListingRepository(application);
        allListings = repository.getAllListings();

    }



    public LiveData<List<Listing>> getAllListings() {
        return allListings;
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
}
