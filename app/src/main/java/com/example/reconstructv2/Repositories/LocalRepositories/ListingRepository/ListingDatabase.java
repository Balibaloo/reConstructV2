package com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.reconstructv2.Models.Listing;

// database settings
@Database(entities = Listing.class, version = 1, exportSchema = false)
public abstract class ListingDatabase extends RoomDatabase {

    private static ListingDatabase instance;

    public abstract ListingDAO listingDAO();

    // returns an instance of the listing database
    public static synchronized ListingDatabase getInstance(Context context) {

        // check if the class variable is null
        if (instance == null) {

            // if the database instance is null create one
            instance = Room.databaseBuilder(context.getApplicationContext(), ListingDatabase.class, "listing_DB")
                    .fallbackToDestructiveMigration()
                    //.addCallback(roomCallback) // add a callback once the databse is set up
                    .build(); // build the database
        }
        return instance;
    }

    // // callback that gets called one the database is crated
    // private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
    //     @Override
    //     public void onCreate(@NonNull SupportSQLiteDatabase db) {
    //         super.onCreate(db);

    //         // populate the database 
    //         new PopulateDBAsync(instance).execute();
    //     }
    // };

    // private static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {
    //     private ListingDAO listingDAO;

    //     private PopulateDBAsync(ListingDatabase db) {
    //         listingDAO = db.listingDAO();
    //     }

    //     @Override
    //     protected Void doInBackground(Void... voids) {
    //         listingDAO.addListing(new Listing("RAndom listing ID","Randpom Autrhor id","BIG TITLE", "body 1 2 3", "2002-07-15T10:30:05.000Z","2002-07-15T10:30:05.000Z",2.0,2.0 ,true, "defaultID"));

    //         return null;
    //     }
    // }

}
