package com.example.reconstructv2.Repositories.LocalRepositories.ListingRepository;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.reconstructv2.Models.Listing;


@Database(entities = Listing.class, version = 1, exportSchema = false)
public abstract class ListingDatabase extends RoomDatabase {

    private static ListingDatabase instance;

    public abstract ListingDAO listingDAO();

    public static synchronized ListingDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), ListingDatabase.class, "temp_listing_DB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsync(instance).execute();
        }
    };

    private static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {
        private ListingDAO listingDAO;

        private PopulateDBAsync(ListingDatabase db) {
            listingDAO = db.listingDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            listingDAO.addListing(new Listing("RAndom listing ID","Randpom Autrhor id","BIG TITLE", "body 1 2 3", "2002-07-15T10:30:05.000Z","2002-07-15T10:30:05.000Z","some Address",true, "someID"));

            return null;
        }
    }

}
