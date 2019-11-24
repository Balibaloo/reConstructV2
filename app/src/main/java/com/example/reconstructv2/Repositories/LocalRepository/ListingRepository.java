package com.example.reconstructv2.Repositories.LocalRepository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.reconstructv2.Models.Listing;

import java.util.List;

public class ListingRepository {
    private ListingDAO listingDAO;
    private LiveData<List<Listing>> allListings;

    public ListingRepository(Application application) {
        ListingDatabase database = ListingDatabase.getInstance(application);
        listingDAO = database.listingDAO();
        allListings = listingDAO.getAllListings();
    }

    public void insert(Listing listing) {
        new InsertListingAsync(listingDAO).execute(listing);
    }


    public void deleteAll() {
        new DeleteAllListingAsync(listingDAO).execute();
    }


    public LiveData<List<Listing>> getAllListings() {
        return allListings;
    }


    private static class InsertListingAsync extends AsyncTask<Listing, Void, Void> {
        private ListingDAO listingDAO;

        private InsertListingAsync(ListingDAO listingDAO) {
            this.listingDAO = listingDAO;

        }

        @Override
        protected Void doInBackground(Listing... listings) {
            listingDAO.addListing(listings[0]);
            return null;
        }
    }

    private static class DeleteAllListingAsync extends AsyncTask<Void, Void, Void> {
        private ListingDAO listingDAO;

        private DeleteAllListingAsync(ListingDAO listingDAO) {
            this.listingDAO = listingDAO;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            listingDAO.deleteAllListings();
            return null;
        }
    }

}
