package com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.reconstructv2.Models.Listing;

import java.util.List;

@Dao
public interface ListingDAO {

    @Insert
    void addListing(Listing listing);

    @Query("DELETE FROM listing_table")
    void deleteAllListings();

    @Query("SELECT * FROM listing_table")
    LiveData<List<Listing>> getAllListings();
}
