package com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.reconstructv2.Models.Listing;

import java.util.List;

// create methods to edit and read the listing database

@Dao
public interface ListingDAO {

    // add a listing to the databse
    @Insert
    void addListing(Listing listing);

    // delete all listings from the database
    @Query("DELETE FROM listing_table")
    void deleteAllListings();

    // select all listings from the database
    @Query("SELECT * FROM listing_table")
    LiveData<List<Listing>> getAllListings();
}
