package com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.example.reconstructv2.Models.Listing;
import java.util.List;

public class ListingRepository {

    private ListingDAO listingDAO;
    private LiveData<List<Listing>> allListings;

    // constructor method
    public ListingRepository(Application application) {

        // get a listingDAO instance from the database instance
        listingDAO = ListingDatabase.getInstance(application).listingDAO();

        // get the list of listings from the database
        allListings = listingDAO.getAllListings();
    }

    // get all listings
    public LiveData<List<Listing>> getAllListings() {
        return allListings;
    }

    // insert a listing into the database
    public void insert(Listing listing) {
        // create and execute a new asyncTask to insert a listing
        new InsertListingAsync(listingDAO).execute(listing);
    }

    // delete all listings from the database
    public void deleteAll() {
        // create and execute a new asyncTask to insert a listing
        new DeleteAllListingAsync(listingDAO).execute();
    }


    // async task to insert a listing into the database
    private static class InsertListingAsync extends AsyncTask<Listing, Void, Void> {
        private ListingDAO listingDAO;

        // the constructor sets the listing dao class variable
        // so that it can be used when the doInBackground method is called
        private InsertListingAsync(ListingDAO listingDAO) {
            this.listingDAO = listingDAO;

        }

        @Override
        protected Void doInBackground(Listing listing) {
            // call the add listing method on the listing dao
            listingDAO.addListing(listings);
            return null;
        }
    }

    // async task to delete all listings from the database
    private static class DeleteAllListingAsync extends AsyncTask<Void, Void, Void> {
        private ListingDAO listingDAO;

        // the constructor sets the listing dao class variable
        // so that it can be used when the doInBackground method is called
        private DeleteAllListingAsync(ListingDAO listingDAO) {
            this.listingDAO = listingDAO;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // call the delete all listings method on the listing dao
            listingDAO.deleteAllListings();
            return null;
        }
    }

}
