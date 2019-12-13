package com.example.reconstructv2.Fragments.SingleListing;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Helpers.UserInfo;
import com.example.reconstructv2.Models.ApiResponses.SingleListingAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class SingleListingViewModel extends AndroidViewModel {
    private MutableLiveData<SingleListingAPIResponse> listingLiveData;

    private APIRepository apiRepository;

    public SingleListingViewModel(@NonNull Application application) {
        super(application);
        apiRepository = new APIRepository(application);
        listingLiveData = apiRepository.getSingleListingAPIResponseMutableLiveData();

    }

    public MutableLiveData<SingleListingAPIResponse> getListingLiveData() {
        return listingLiveData;
    }

    public void fetchListing(Context context, String listingID){
        System.out.println("is user logged in?" + UserInfo.getIsLoggedIn(context));

        if (UserInfo.getIsLoggedIn(context)) {
            System.out.println(UserInfo.getToken(context));
            apiRepository.getListingAuthenticated("Bearer " + UserInfo.getToken(context), listingID);
        } else {
            apiRepository.getListingNoAuth(listingID);
        }
    }


}
