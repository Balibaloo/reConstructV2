package com.example.reconstructv2.Fragments.CreateListing.CreateListingMain;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.ListingIDAPIResponse;
import com.example.reconstructv2.Models.ListingFull;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

import io.reactivex.annotations.NonNull;

public class CreateListingMainViewModel extends AndroidViewModel {

        private APIRepository apiRepository;

        private MutableLiveData<ListingIDAPIResponse> listingIDAPIResponse;


        public CreateListingMainViewModel(@NonNull Application application) {
            super(application);

            apiRepository = new APIRepository(application);
            listingIDAPIResponse = apiRepository.getListingIDAPIResponseMutableLiveData();

        }


        public MutableLiveData<ListingIDAPIResponse> getListingIDAPIResponse() {
            return listingIDAPIResponse;
        }

        public void createListingRequest(ListingFull listing) {
            apiRepository.createListing("Bearer " + UserInfo.getToken(getApplication()),listing);
        }



    }
