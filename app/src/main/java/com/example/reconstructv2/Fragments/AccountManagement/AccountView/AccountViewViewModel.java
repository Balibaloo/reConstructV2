package com.example.reconstructv2.Fragments.AccountManagement.AccountView;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.reconstructv2.Models.ApiResponses.UserAPIResponse;
import com.example.reconstructv2.Repositories.RemoteRepository.APIRepository;

public class AccountViewViewModel extends AndroidViewModel {

    private APIRepository apiRepository;

    private MutableLiveData<UserAPIResponse> userAPIResponse;


    public AccountViewViewModel(@NonNull Application application) {
        super(application);

        // fetch a new instance of the API repository
        apiRepository = new APIRepository(application);

        userAPIResponse = apiRepository.getUserAPIResponseMutableLiveData();

    }

    // Getters for MutableLiveData

    public MutableLiveData<UserAPIResponse> getUserAPIResponseMutableLiveData() {
        return userAPIResponse;
    }

    // functions that send requests to the server

    public void sendGetUserRequest(String userID){
        apiRepository.getUserProfile(userID);
    }


}
